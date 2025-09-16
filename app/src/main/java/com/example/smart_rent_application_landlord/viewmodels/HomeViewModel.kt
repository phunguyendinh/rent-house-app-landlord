// HomeViewModel.kt - Thêm logic quản lý selectedServices cho room
package com.example.smart_rent_application_landlord.viewmodels

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smart_rent_application_landlord.models.Building
import com.example.smart_rent_application_landlord.models.BuildingType
import com.example.smart_rent_application_landlord.models.BuildingCount
import com.example.smart_rent_application_landlord.models.BuildingRequest
import com.example.smart_rent_application_landlord.models.ContractWithTenant
import com.example.smart_rent_application_landlord.models.CreateInvitationRequest
import com.example.smart_rent_application_landlord.models.RevenueStatsResponse
import com.example.smart_rent_application_landlord.models.Room
import com.example.smart_rent_application_landlord.models.RoomRequest
import com.example.smart_rent_application_landlord.models.RoomServiceRequest
import com.example.smart_rent_application_landlord.models.Service
import com.example.smart_rent_application_landlord.models.ServiceRequest
import com.example.smart_rent_application_landlord.models.ServiceUpdateRequest
import com.example.smart_rent_application_landlord.models.TenantContract
import com.example.smart_rent_application_landlord.models.TenantDetail
import com.example.smart_rent_application_landlord.models.TenantServiceBill1
import com.example.smart_rent_application_landlord.services.RetrofitClient
import com.example.smart_rent_application_landlord.services.ServiceBillDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

// THÊM MỚI: Dashboard Statistics Data Classes
data class DashboardStats(
    val totalTenants: Int = 0,
    val emptyRooms: Int = 0,
    val totalRooms: Int = 0,
    val totalRevenue: Double = 0.0,
    val monthlyRevenue: Double = 0.0
)

class HomeViewModel : ViewModel() {
    private val _buildings = MutableStateFlow<List<Building>>(emptyList())
    val buildings: StateFlow<List<Building>> = _buildings

    private val _buildingTypes = MutableStateFlow<List<BuildingType>>(emptyList())
    val buildingTypes: StateFlow<List<BuildingType>> = _buildingTypes

    // THÊM MỚI: StateFlow cho building counts
    private val _buildingCounts = MutableStateFlow<List<BuildingCount>>(emptyList())
    val buildingCounts: StateFlow<List<BuildingCount>> = _buildingCounts

    private val _rooms = MutableStateFlow<List<Room>>(emptyList())
    val rooms: StateFlow<List<Room>> = _rooms

    private val _currentBuilding = MutableStateFlow<Building?>(null)
    val currentBuilding: StateFlow<Building?> = _currentBuilding.asStateFlow()

    private val _services = MutableStateFlow<List<Service>>(emptyList())
    val services: StateFlow<List<Service>> = _services.asStateFlow()

    private val _roomsByService = MutableStateFlow<List<Room>>(emptyList())
    val roomsByService: StateFlow<List<Room>> = _roomsByService.asStateFlow()

    // THÊM MỚI: StateFlow cho selected services khi thêm room
    private val _selectedRoomServices = mutableStateOf<List<Service>>(emptyList())
    val selectedRoomServices: State<List<Service>> = _selectedRoomServices

    private val _tenantsByBuilding = MutableStateFlow<List<TenantContract>>(emptyList())
    val tenantsByBuilding: StateFlow<List<TenantContract>> = _tenantsByBuilding.asStateFlow()

    private val _tenantDetail = MutableStateFlow<TenantDetail?>(null)
    val tenantDetail: StateFlow<TenantDetail?> = _tenantDetail.asStateFlow()

    private val _dashboardStats = MutableStateFlow(DashboardStats())
    val dashboardStats: StateFlow<DashboardStats> = _dashboardStats.asStateFlow()

    private val LANDLORD_ID = 1

    init {
        fetchBuildings(1)
        fetchBuildingTypes()
        fetchBuildingCounts(1) // Lấy số lượng buildings theo type
        fetchServices()
        fetchDashboardStats(1) // THÊM MỚI: Fetch dashboard statistics
    }

    // THÊM MỚI: Function để toggle service cho room
    fun toggleRoomService(service: Service) {
        val currentServices = _selectedRoomServices.value.toMutableList()
        if (currentServices.any { it.id == service.id }) {
            currentServices.removeAll { it.id == service.id }
        } else {
            currentServices.add(service)
        }
        _selectedRoomServices.value = currentServices
    }

    // THÊM MỚI: Function để clear selected services
    fun clearSelectedRoomServices() {
        _selectedRoomServices.value = emptyList()
    }

    // THÊM MỚI: Function để set selected services (nếu cần)
    fun setSelectedRoomServices(services: List<Service>) {
        _selectedRoomServices.value = services
    }

    fun fetchBuildings(landlordId: Int) {
        viewModelScope.launch {
            try {
                val fetchedBuildings = RetrofitClient.buildingApiService.getBuildingsByLandlordId(landlordId)
                Log.d("HomeViewModel", "Fetched buildings: $fetchedBuildings")
                _buildings.value = fetchedBuildings
            } catch (e: Exception) {
                e.printStackTrace()
                _buildings.value = emptyList()
            }
        }
    }

    fun fetchBuildingsByType(landlordId: Int, typeId: Int) {
        viewModelScope.launch {
            try {
                val fetchedBuildings = RetrofitClient.buildingApiService.getBuildingsByLandlordAndType(landlordId, typeId)
                Log.d("HomeViewModel", "Fetched buildings by type: $fetchedBuildings")
                _buildings.value = fetchedBuildings
            } catch (e: Exception) {
                e.printStackTrace()
                _buildings.value = emptyList()
            }
        }
    }

    // THÊM MỚI: Function lấy building counts
    private fun fetchBuildingCounts(landlordId: Int) {
        viewModelScope.launch {
            try {
                val fetchedCounts = RetrofitClient.buildingApiService.getBuildingCountsByLandlord(landlordId)
                Log.d("HomeViewModel", "Fetched building counts: $fetchedCounts")
                _buildingCounts.value = fetchedCounts
            } catch (e: Exception) {
                e.printStackTrace()
                _buildingCounts.value = emptyList()
            }
        }
    }

    fun filterBuildings(landlordId: Int, selectedTypeName: String) {
        if (selectedTypeName == "Tất cả") {
            fetchBuildings(landlordId)
        } else {
            val selectedType = _buildingTypes.value.find { it.name == selectedTypeName }
            selectedType?.let { type ->
                fetchBuildingsByType(landlordId, type.id)
            }
        }
    }

    fun fetchBuildingTypes() {
        viewModelScope.launch {
            try {
                val fetchedBuildingTypes = RetrofitClient.buildingTypeApiService.getAllBuildingTypes()
                Log.d("HomeViewModel", "Fetched building types: $fetchedBuildingTypes")
                _buildingTypes.value = fetchedBuildingTypes
            } catch (e: Exception) {
                e.printStackTrace()
                _buildingTypes.value = emptyList()
            }
        }
    }

    // THÊM MỚI: Function lấy rooms theo building_id
    fun fetchRoomsByBuildingId(buildingId: Int) {
        viewModelScope.launch {
            try {
                val fetchedRooms = RetrofitClient.roomApiService.getRoomsByBuildingId(buildingId)
                Log.d("HomeViewModel", "Fetched rooms: $fetchedRooms")
                _rooms.value = fetchedRooms
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error fetching rooms", e)
                _rooms.value = emptyList()
            }
        }
    }

    // Thêm function fetch building
    fun fetchBuildingById(buildingId: Int) {
        viewModelScope.launch {
            try {
                val building = RetrofitClient.buildingApiService.getBuildingById(buildingId)
                _currentBuilding.value = building
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error fetching building: ${e.message}")
            }
        }
    }

    // Thay đổi function này thành không cần parameter
    fun fetchServices() {
        viewModelScope.launch {
            try {
                val servicesList = RetrofitClient.serviceApiService.getServicesByLandlordId(LANDLORD_ID)
                _services.value = servicesList
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error fetching services: ${e.message}")
            }
        }
    }

    fun addService(name: String, unit: String, pricePerUnit: Double, isMandatory: Boolean) {
        viewModelScope.launch {
            try {
                val singleUnit = unit.lowercase() !in listOf("kwh", "khối")

                val serviceRequest = ServiceRequest(
                    landlord_id = LANDLORD_ID,
                    name = name,
                    unit = unit,
                    price_per_unit = pricePerUnit,
                    is_mandatory = isMandatory,
                    single_unit = singleUnit
                )

                val response = RetrofitClient.serviceApiService.addServiceWithRooms(serviceRequest)
                if (response.isSuccess) {
                    fetchServices() // Refresh danh sách
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error adding service: ${e.message}")
            }
        }
    }

    // Thêm vào HomeViewModel.kt

    // Thêm function updateService
    fun updateService(
        serviceId: Int,
        name: String,
        unit: String,
        pricePerUnit: Double,
        isMandatory: Boolean
    ) {
        viewModelScope.launch {
            try {
                val singleUnit = unit.lowercase() !in listOf("kwh", "khối")

                val serviceUpdateRequest = ServiceUpdateRequest(
                    id = serviceId,
                    landlord_id = LANDLORD_ID,
                    name = name,
                    unit = unit,
                    price_per_unit = pricePerUnit,
                    is_mandatory = isMandatory,
                    single_unit = singleUnit
                )

                val response = RetrofitClient.serviceApiService.updateServiceWithRooms(serviceUpdateRequest)
                if (response.isSuccess) {
                    fetchServices() // Refresh danh sách
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error updating service: ${e.message}")
            }
        }
    }

    // THÊM MỚI: Function xóa service
    fun deleteService(serviceId: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.serviceApiService.deleteServiceWithRooms(serviceId)
                if (response.isSuccess) {
                    // Refresh lại danh sách services
                    fetchServices()
                    Log.d("HomeViewModel", "Service deleted successfully")
                } else {
                    Log.e("HomeViewModel", "Failed to delete service: ${response.message}")
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error deleting service: ${e.message}")
            }
        }
    }

    // THÊM MỚI: Function lấy rooms theo service_id
    fun fetchRoomsByServiceId(serviceId: Int) {
        viewModelScope.launch {
            try {
                val fetchedRooms = RetrofitClient.roomApiService.getRoomsByServiceId(serviceId)
                Log.d("HomeViewModel", "Fetched rooms by service: $fetchedRooms")
                _roomsByService.value = fetchedRooms
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error fetching rooms by service: ${e.message}")
                _roomsByService.value = emptyList()
            }
        }
    }

    // Function clear rooms khi không cần thiết
    fun clearRoomsByService() {
        _roomsByService.value = emptyList()
    }

    fun removeServiceFromRoom(roomId: Int, serviceId: Int) {
        viewModelScope.launch {
            try {
                // Cập nhật UI ngay lập tức bằng cách xóa room khỏi local state
                val currentRooms = _roomsByService.value.toMutableList()
                val updatedRooms = currentRooms.filter { it.id != roomId }
                _roomsByService.value = updatedRooms

                // Gọi API để xóa trên server
                val response = RetrofitClient.serviceApiService
                    .deleteRoomServiceByRoomAndService(roomId, serviceId)

                if (response.isSuccess) {
                    Log.d("HomeViewModel", "Service removed from room successfully")

                    // THÊM: Fetch lại services để cập nhật is_mandatory
                    fetchServices()

                    // THÊM: Fetch lại rooms by service để đồng bộ
                    fetchRoomsByServiceId(serviceId)

                    // Nếu đang xem rooms của building cụ thể, refresh luôn
                    _currentBuilding.value?.let { building ->
                        fetchRoomsByBuildingId(building.id)
                    }
                } else {
                    Log.e("HomeViewModel", "Failed to remove service from room")
                    // Nếu API thất bại, khôi phục lại state cũ
                    _roomsByService.value = currentRooms
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error removing service from room: ${e.message}")
                // Nếu có exception, fetch lại từ server để đảm bảo consistency
                fetchRoomsByServiceId(serviceId)
                // THÊM: Fetch services để cập nhật switch
                fetchServices()
            }
        }
    }

    // THÊM VÀO HomeViewModel.kt - uploadImage function với debug chi tiết
    fun uploadImage(
        context: Context,
        uri: Uri,
        onSuccess: (String, String) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val bytes = inputStream?.readBytes()
                inputStream?.close()

                if (bytes == null) {
                    withContext(Dispatchers.Main) {
                        onError("Không thể đọc file ảnh")
                    }
                    return@launch
                }

                val requestFile = bytes.toRequestBody("image/*".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData(
                    "image",
                    "building_image_${System.currentTimeMillis()}.jpg",
                    requestFile
                )

                Log.d("HomeViewModel", "Starting image upload...")

                val response = RetrofitClient.uploadApiService.uploadImage(body)

                // DEBUG CHI TIẾT
                Log.d("HomeViewModel", "=== UPLOAD RESPONSE DEBUG ===")
                Log.d("HomeViewModel", "Response object: $response")
                Log.d("HomeViewModel", "isSuccess: ${response.isSuccess}")
                Log.d("HomeViewModel", "message: '${response.message}'")
                Log.d("HomeViewModel", "imageUrl: '${response.imageUrl}'")
                Log.d("HomeViewModel", "imageUrl is null: ${response.imageUrl == null}")
                Log.d("HomeViewModel", "imageUrl is empty: ${response.imageUrl?.isEmpty()}")
                Log.d("HomeViewModel", "filename: '${response.filename}'")
                Log.d("HomeViewModel", "filename is null: ${response.filename == null}")
                Log.d("HomeViewModel", "=== END DEBUG ===")

                if (response.isSuccess && response.imageUrl != null && response.filename != null) {
                    val imageUrl = response.imageUrl
                    val filename = response.filename

                    Log.d("HomeViewModel", "SUCCESS - About to call onSuccess with:")
                    Log.d("HomeViewModel", "  imageUrl: '$imageUrl'")
                    Log.d("HomeViewModel", "  filename: '$filename'")

                    withContext(Dispatchers.Main) {
                        onSuccess(imageUrl, filename)
                    }
                } else {
                    Log.e("HomeViewModel", "FAILED CONDITIONS:")
                    Log.e("HomeViewModel", "  isSuccess: ${response.isSuccess}")
                    Log.e("HomeViewModel", "  imageUrl null: ${response.imageUrl == null}")
                    Log.e("HomeViewModel", "  filename null: ${response.filename == null}")

                    withContext(Dispatchers.Main) {
                        onError(response.message ?: "Upload thất bại")
                    }
                }

            } catch (e: Exception) {
                Log.e("HomeViewModel", "Upload exception: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    onError("Lỗi upload: ${e.localizedMessage ?: "Không xác định"}")
                }
            }
        }
    }

    // Updated addBuilding function
    fun addBuilding(
        landlordId: Int,
        name: String,
        address: String,
        typeId: Int,
        billingDate: Int,
        imageUrl: String = "",
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val buildingRequest = BuildingRequest(
                    landlord_id = landlordId,
                    name = name,
                    address = address,
                    type_id = typeId,
                    billing_date = billingDate,
                    image_url = imageUrl
                )

                Log.d("HomeViewModel", "Sending request: $buildingRequest")

                val response = RetrofitClient.buildingApiService.addBuilding(buildingRequest)

                Log.d("HomeViewModel", "API Response: $response")

                // Check both isSuccess and success fields
                val isSuccessful = response.isSuccess == true ||
                        response.message?.contains("successfully", ignoreCase = true) == true

                if (isSuccessful) {
                    // Refresh data
                    fetchBuildings(landlordId)
                    fetchBuildingCounts(landlordId)

                    withContext(Dispatchers.Main) {
                        onSuccess()
                    }
                } else {
                    Log.e("HomeViewModel", "API returned error: ${response.message}")
                    withContext(Dispatchers.Main) {
                        onError(response.message ?: "Có lỗi xảy ra khi thêm building")
                    }
                }

            } catch (e: Exception) {
                Log.e("HomeViewModel", "Exception occurred: ${e.message}", e)

                // Still refresh to check if it was added
                try {
                    fetchBuildings(landlordId)
                    fetchBuildingCounts(landlordId)
                } catch (refreshException: Exception) {
                    Log.e("HomeViewModel", "Failed to refresh: ${refreshException.message}")
                }

                withContext(Dispatchers.Main) {
                    onError("Lỗi kết nối: ${e.localizedMessage ?: "Không xác định"}")
                }
            }
        }
    }

    fun addRoom(
        buildingId: Int,
        name: String,
        rentPrice: Double,
        maxOccupants: Int,
        imageUrl: String,
        selectedServices: List<Service>,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                // Tạo room request
                val roomRequest = RoomRequest(
                    name = name,
                    building_id = buildingId,
                    rent_price = rentPrice,
                    max_occupants = maxOccupants,
                    image_url = imageUrl
                )

                // Gọi API tạo room
                val response = RetrofitClient.roomApiService.addRoom(roomRequest)

                if (response.isSuccess) {
                    // Nếu có services được chọn, cần thêm room-service relationships
                    if (selectedServices.isNotEmpty()) {
                        // Lấy room_id từ response hoặc fetch lại rooms để lấy room mới nhất
                        // Vì API response không trả về room_id, ta cần một cách khác
                        // Có thể cần sửa API để trả về room_id hoặc fetch lại rooms

                        // Tạm thời fetch lại rooms để lấy room mới nhất
                        val rooms = RetrofitClient.roomApiService.getRoomsByBuildingId(buildingId)
                        val newRoom = rooms.maxByOrNull { it.id } // Lấy room có ID lớn nhất (mới nhất)

                        if (newRoom != null) {
                            // Thêm services cho room
                            selectedServices.forEach { service ->
                                try {
                                    val roomServiceRequest = RoomServiceRequest(
                                        room_id = newRoom.id,
                                        service_id = service.id
                                    )
                                    RetrofitClient.roomServiceApiService.addRoomService(roomServiceRequest)
                                } catch (e: Exception) {
                                    Log.e("HomeViewModel", "Error adding service ${service.id} to room: ${e.message}")
                                }
                            }
                        }
                    }

                    // Refresh room list
                    fetchRoomsByBuildingId(buildingId)

                    withContext(Dispatchers.Main) {
                        onSuccess()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        onError(response.message ?: "Có lỗi xảy ra khi thêm phòng")
                    }
                }

            } catch (e: Exception) {
                Log.e("HomeViewModel", "Exception in addRoom: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    onError("Lỗi kết nối: ${e.localizedMessage ?: "Không xác định"}")
                }
            }
        }
    }
    // Function fetch tenants theo building
    fun fetchTenantsByBuildingId(buildingId: Int) {
        viewModelScope.launch {
            try {
                val tenants = RetrofitClient.contractApiService.getTenantsByBuildingId(buildingId)
                Log.d("HomeViewModel", "Fetched tenants: $tenants")
                _tenantsByBuilding.value = tenants
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error fetching tenants: ${e.message}")
                _tenantsByBuilding.value = emptyList()
            }
        }
    }

    // Function clear tenants
    fun clearTenantsByBuilding() {
        _tenantsByBuilding.value = emptyList()
    }

    fun fetchTenantDetailById(tenantId: Int) {
        viewModelScope.launch {
            try {
                val detail = RetrofitClient.tenantApiService.getTenantDetailById(tenantId)
                _tenantDetail.value = detail
                Log.d("HomeViewModel", "Fetched tenant detail: $detail")
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error fetching tenant detail: ${e.message}")
                _tenantDetail.value = null
            }
        }
    }

    fun clearTenantDetail() {
        _tenantDetail.value = null
    }

    // Thêm vào HomeViewModel.kt
    private val _serviceBillDetail = MutableStateFlow<ServiceBillDetail?>(null)
    val serviceBillDetail: StateFlow<ServiceBillDetail?> = _serviceBillDetail.asStateFlow()

    // Thêm vào HomeViewModel.kt trong fetchServiceBillDetail
    fun fetchServiceBillDetail(billId: Int) {
        viewModelScope.launch {
            try {
                Log.d("HomeViewModel", "Fetching service bill detail for billId: $billId")
                val detail = RetrofitClient.serviceBillApiService.getServiceBillWithDetails(billId)
                _serviceBillDetail.value = detail
                Log.d("HomeViewModel", "Fetched service bill detail: $detail")
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error fetching service bill detail: ${e.message}")
                _serviceBillDetail.value = null
            }
        }
    }

    fun clearServiceBillDetail() {
        _serviceBillDetail.value = null
    }

    // Thêm vào HomeViewModel.kt
    fun markServiceBillAsPaid(
        billId: Int,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.serviceBillApiService.markServiceBillAsPaid(billId)

                if (response.isSuccess) {
                    // Refresh lại chi tiết bill để cập nhật UI
                    fetchServiceBillDetail(billId)

                    withContext(Dispatchers.Main) {
                        onSuccess()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        onError(response.message)
                    }
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error marking bill as paid: ${e.message}")
                withContext(Dispatchers.Main) {
                    onError("Lỗi kết nối: ${e.localizedMessage ?: "Không xác định"}")
                }
            }
        }
    }

    // THÊM MỚI: StateFlow cho current room
    private val _currentRoom = MutableStateFlow<Room?>(null)
    val currentRoom: StateFlow<Room?> = _currentRoom.asStateFlow()

    // THÊM MỚI: StateFlow cho service bills của room
    private val _roomServiceBills = MutableStateFlow<List<TenantServiceBill1>>(emptyList())
    val roomServiceBills: StateFlow<List<TenantServiceBill1>> = _roomServiceBills.asStateFlow()

    // THÊM MỚI: StateFlow cho active contract của room
    private val _activeContract = MutableStateFlow<ContractWithTenant?>(null)
    val activeContract: StateFlow<ContractWithTenant?> = _activeContract.asStateFlow()

    fun fetchRoomById(roomId: Int) {
        viewModelScope.launch {
            try {
                val room = RetrofitClient.roomApiService.getRoomById(roomId)
                _currentRoom.value = room
                Log.d("HomeViewModel", "Fetched room: $room")
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error fetching room: ${e.message}")
                _currentRoom.value = null
            }
        }
    }

    // THÊM MỚI: Function lấy service bills theo room ID
    fun fetchServiceBillsByRoomId(roomId: Int) {
        viewModelScope.launch {
            try {
                val bills = RetrofitClient.serviceBillApiService.getServiceBillsByRoomId(roomId)
                _roomServiceBills.value = bills
                Log.d("HomeViewModel", "Fetched service bills for room: $bills")
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error fetching service bills for room: ${e.message}")
                _roomServiceBills.value = emptyList()
            }
        }
    }

    // THÊM MỚI: Function lấy active contract theo room ID
    fun fetchActiveContractByRoomId(roomId: Int) {
        viewModelScope.launch {
            try {
                val contract = RetrofitClient.contractApiService.getActiveContractByRoomId(roomId)
                _activeContract.value = contract
                Log.d("HomeViewModel", "Fetched active contract: $contract")
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error fetching active contract: ${e.message}")
                _activeContract.value = null
            }
        }
    }

    // Function clear room data
    fun clearCurrentRoom() {
        _currentRoom.value = null
        _roomServiceBills.value = emptyList()
        _activeContract.value = null
    }

    // THÊM MỚI: Function fetch dashboard statistics
    private fun fetchDashboardStats(landlordId: Int) {
        viewModelScope.launch {
            try {
                // Fetch tất cả dữ liệu cần thiết song song
                val tenantsDeferred = async { fetchTotalTenantsByLandlord(landlordId) }
                val roomsStatsDeferred = async { fetchRoomStatsByLandlord(landlordId) }
                val revenueDeferred = async { fetchRevenueStatsByLandlord(landlordId) }

                val totalTenants = tenantsDeferred.await()
                val roomsStats = roomsStatsDeferred.await()
                val revenueStats = revenueDeferred.await()

                _dashboardStats.value = DashboardStats(
                    totalTenants = totalTenants,
                    emptyRooms = roomsStats.first,
                    totalRooms = roomsStats.second,
                    totalRevenue = revenueStats.first,
                    monthlyRevenue = revenueStats.second
                )

                Log.d("HomeViewModel", "Dashboard stats updated: ${_dashboardStats.value}")

            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error fetching dashboard stats: ${e.message}")
                _dashboardStats.value = DashboardStats() // Reset to default values
            }
        }
    }

    // THÊM MỚI: Function đếm tổng số tenant theo landlord
    private suspend fun fetchTotalTenantsByLandlord(landlordId: Int): Int {
        return try {
            // Gọi API để lấy tổng số tenant trong tất cả buildings của landlord
            // API endpoint: /dashboard/landlord/{landlord_id}/tenants/count
            val response = RetrofitClient.dashboardApiService.getTenantCountByLandlord(landlordId)
            response.count
        } catch (e: Exception) {
            Log.e("HomeViewModel", "Error fetching tenant count: ${e.message}")
            0
        }
    }

    // THÊM MỚI: Function lấy thống kê phòng theo landlord
    private suspend fun fetchRoomStatsByLandlord(landlordId: Int): Pair<Int, Int> {
        return try {
            // Gọi API để lấy thống kê phòng trống và tổng phòng
            // API endpoint: /dashboard/landlord/{landlord_id}/rooms/stats
            val response = RetrofitClient.dashboardApiService.getRoomStatsByLandlord(landlordId)
            Pair(response.emptyRooms, response.totalRooms)
        } catch (e: Exception) {
            Log.e("HomeViewModel", "Error fetching room stats: ${e.message}")
            Pair(0, 0)
        }
    }

    // THÊM MỚI: Function lấy thống kê doanh thu theo landlord
    private suspend fun fetchRevenueStatsByLandlord(landlordId: Int): Pair<Double, Double> {
        return try {
            // Gọi API để lấy tổng doanh thu và doanh thu tháng hiện tại
            // API endpoint: /dashboard/landlord/{landlord_id}/revenue/stats
            val response = RetrofitClient.dashboardApiService.getRevenueStatsByLandlord(landlordId)
            Pair(response.totalRevenue, response.monthlyRevenue)
        } catch (e: Exception) {
            Log.e("HomeViewModel", "Error fetching revenue stats: ${e.message}")
            Pair(0.0, 0.0)
        }
    }

    // THÊM MỚI: Function refresh dashboard stats
    fun refreshDashboardStats() {
        fetchDashboardStats(LANDLORD_ID)
    }

    private val _buildingRevenueStats = MutableStateFlow(RevenueStatsResponse(true, 0.0, 0.0))
    val buildingRevenueStats: StateFlow<RevenueStatsResponse> = _buildingRevenueStats

    fun fetchBuildingRevenueStats(buildingId: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.dashboardApiService.getRevenueStatsByBuilding(buildingId)
                // response đã là RevenueStatsResponse rồi
                if (response.success) {
                    _buildingRevenueStats.value = response
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // Sửa lại function createRoomInvitation trong HomeViewModel
    fun createRoomInvitation(
        roomId: Int,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                // Tạo một đối tượng của data class thay vì Map
                val requestBody = CreateInvitationRequest(
                    room_id = roomId,
                    landlord_id = LANDLORD_ID,
                    expires_in_days = 7,
                    max_uses = 1
                )

                val response = RetrofitClient.roomInvitationApiService.createRoomInvitation(requestBody)

                if (response.isSuccess && response.data != null) {
                    onSuccess(response.data.invitation_code)
                } else {
                    onError(response.message ?: "Không thể tạo mã mời")
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error creating room invitation: ${e.message}")
                onError("Lỗi kết nối: ${e.message}")
            }
        }
    }

}