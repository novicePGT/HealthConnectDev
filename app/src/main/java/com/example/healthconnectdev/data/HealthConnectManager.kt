package com.example.healthconnectdev.data

import android.content.Context
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.runtime.mutableStateOf
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.HealthConnectClient.Companion.SDK_UNAVAILABLE
import androidx.health.connect.client.HealthConnectClient.Companion.SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED
import androidx.health.connect.client.HealthConnectClient.Companion.sdkStatus
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.records.ActiveCaloriesBurnedRecord
import androidx.health.connect.client.records.BasalBodyTemperatureRecord
import androidx.health.connect.client.records.BasalMetabolicRateRecord
import androidx.health.connect.client.records.BloodGlucoseRecord
import androidx.health.connect.client.records.BloodPressureRecord
import androidx.health.connect.client.records.BodyFatRecord
import androidx.health.connect.client.records.BodyTemperatureRecord
import androidx.health.connect.client.records.BodyWaterMassRecord
import androidx.health.connect.client.records.BoneMassRecord
import androidx.health.connect.client.records.CervicalMucusRecord
import androidx.health.connect.client.records.CyclingPedalingCadenceRecord
import androidx.health.connect.client.records.DistanceRecord
import androidx.health.connect.client.records.ElevationGainedRecord
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.records.FloorsClimbedRecord
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.HeartRateVariabilityRmssdRecord
import androidx.health.connect.client.records.HeightRecord
import androidx.health.connect.client.records.HydrationRecord
import androidx.health.connect.client.records.IntermenstrualBleedingRecord
import androidx.health.connect.client.records.LeanBodyMassRecord
import androidx.health.connect.client.records.NutritionRecord
import androidx.health.connect.client.records.OxygenSaturationRecord
import androidx.health.connect.client.records.PowerRecord
import androidx.health.connect.client.records.RespiratoryRateRecord
import androidx.health.connect.client.records.RestingHeartRateRecord
import androidx.health.connect.client.records.SleepSessionRecord
import androidx.health.connect.client.records.SpeedRecord
import androidx.health.connect.client.records.StepsCadenceRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.WeightRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import java.time.Instant

class HealthConnectManager(private val context: Context) {
    private val healthConnectClient by lazy { HealthConnectClient.getOrCreate(context = context) }

    /**
     * 지금은 INSTALLED 상태로 초기화합니다.
     * 현재 문제점은 시점이 맞지 않아서 뷰가 생성되고 난 후 INSTALLED 로 감지하는 문제가 있습니다.
     */
    var availability = mutableStateOf(HealthConnectAvailability.INSTALLED)
        private set

    fun checkAvailability() {
        availability.value = when(sdkStatus(context)) {
            SDK_UNAVAILABLE -> HealthConnectAvailability.NOT_INSTALLED
            SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED -> HealthConnectAvailability.NOT_SUPPORTED
            else -> HealthConnectAvailability.INSTALLED
        }
    }

    suspend fun hasAllPermissions(permissions: Set<String>): Boolean {
        return healthConnectClient.permissionController.getGrantedPermissions().containsAll(permissions)
    }

    fun requestPermissions(): ActivityResultContract<Set<String>, Set<String>> {
        return PermissionController.createRequestPermissionResultContract()
    }

    /**
     * 심박수
     */
    suspend fun readHeartRate(start: Instant, end: Instant): List<HeartRateRecord> {
        val request = ReadRecordsRequest(
            recordType = HeartRateRecord::class,
            timeRangeFilter = TimeRangeFilter.between(start, end)
        )
        val response = healthConnectClient.readRecords(request = request)
        return response.records
    }

    /**
     * 활성 칼로리
     */
    suspend fun readActiveCaloriesBurnedRecord(start: Instant, end: Instant): List<ActiveCaloriesBurnedRecord> {
        val request = ReadRecordsRequest(
            recordType = ActiveCaloriesBurnedRecord::class,
            timeRangeFilter = TimeRangeFilter.between(start, end)
        )
        val response = healthConnectClient.readRecords(request = request)
        return response.records
    }

    /**
     * 기초신체온도기록
     */
    suspend fun readBasalBodyTemperatureRecord(start: Instant, end: Instant): List<BasalBodyTemperatureRecord> {
        val request = ReadRecordsRequest(
            recordType = BasalBodyTemperatureRecord::class,
            timeRangeFilter = TimeRangeFilter.between(start, end)
        )
        val response = healthConnectClient.readRecords(request = request)
        return response.records
    }

    /**
     * 기초대사율기록
     */
    suspend fun readBasalMetabolicRateRecord(start: Instant, end: Instant): List<BasalMetabolicRateRecord> {
        val request = ReadRecordsRequest(
            recordType = BasalMetabolicRateRecord::class,
            timeRangeFilter = TimeRangeFilter.between(start, end)
        )
        val response = healthConnectClient.readRecords(request = request)
        return response.records
    }

    /**
     * 혈당기록
     */
    suspend fun readBloodGlucoseRecord(start: Instant, end: Instant): List<BloodGlucoseRecord> {
        val request = ReadRecordsRequest(
            recordType = BloodGlucoseRecord::class,
            timeRangeFilter = TimeRangeFilter.between(start, end)
        )
        val response = healthConnectClient.readRecords(request = request)
        return response.records
    }

    /**
     * 혈압기록
     */
    suspend fun readBloodPressureRecord(start: Instant, end: Instant): List<BloodPressureRecord> {
        val request = ReadRecordsRequest(
            recordType = BloodPressureRecord::class,
            timeRangeFilter = TimeRangeFilter.between(start, end)
        )
        val response = healthConnectClient.readRecords(request = request)
        return response.records
    }

    /**
     * 신체지방기록
     */
    suspend fun readBodyFatRecord(start: Instant, end: Instant): List<BodyFatRecord> {
        val request = ReadRecordsRequest(
            recordType = BodyFatRecord::class,
            timeRangeFilter = TimeRangeFilter.between(start, end)
        )
        val response = healthConnectClient.readRecords(request = request)
        return response.records
    }

    /**
     * 신체온도기록
     */
    suspend fun readBodyTemperatureRecord(start: Instant, end: Instant): List<BodyTemperatureRecord> {
        val request = ReadRecordsRequest(
            recordType = BodyTemperatureRecord::class,
            timeRangeFilter = TimeRangeFilter.between(start, end)
        )
        val response = healthConnectClient.readRecords(request = request)
        return response.records
    }

    /**
     * 체수분량기록
     */
    suspend fun readBodyWaterMassRecord(start: Instant, end: Instant): List<BodyWaterMassRecord> {
        val request = ReadRecordsRequest(
            recordType = BodyWaterMassRecord::class,
            timeRangeFilter = TimeRangeFilter.between(start, end)
        )
        val response = healthConnectClient.readRecords(request = request)
        return response.records
    }

    /**
     * 골량..?기록
     */
    suspend fun readBoneMassRecord(start: Instant, end: Instant): List<BoneMassRecord> {
        val request = ReadRecordsRequest(
            recordType = BoneMassRecord::class,
            timeRangeFilter = TimeRangeFilter.between(start, end)
        )
        val response = healthConnectClient.readRecords(request = request)
        return response.records
    }

    /**
     * 자궁경부점액기록...
     */
    suspend fun readCervicalMucusRecord(start: Instant, end: Instant): List<CervicalMucusRecord> {
        val request = ReadRecordsRequest(
            recordType = CervicalMucusRecord::class,
            timeRangeFilter = TimeRangeFilter.between(start, end)
        )
        val response = healthConnectClient.readRecords(request = request)
        return response.records
    }

    /**
     * 사이클링 페달링 케이던스 기록
     */
    suspend fun readCyclingPedalingCadenceRecord(start: Instant, end: Instant): List<CyclingPedalingCadenceRecord> {
        val request = ReadRecordsRequest(
            recordType = CyclingPedalingCadenceRecord::class,
            timeRangeFilter = TimeRangeFilter.between(start, end)
        )
        val response = healthConnectClient.readRecords(request = request)
        return response.records
    }

    /**
     * 거리기록
     */
    suspend fun readDistanceRecord(start: Instant, end: Instant): List<DistanceRecord> {
        val request = ReadRecordsRequest(
            recordType = DistanceRecord::class,
            timeRangeFilter = TimeRangeFilter.between(start, end)
        )
        val response = healthConnectClient.readRecords(request = request)
        return response.records
    }

    /**
     * 고도상승기록(층계?)
     */
    suspend fun readElevationGainedRecord(start: Instant, end: Instant): List<ElevationGainedRecord> {
        val request = ReadRecordsRequest(
            recordType = ElevationGainedRecord::class,
            timeRangeFilter = TimeRangeFilter.between(start, end)
        )
        val response = healthConnectClient.readRecords(request = request)
        return response.records
    }

    /**
     * 운동기록
     */
    suspend fun readExerciseSessionRecord(start: Instant, end: Instant): List<ExerciseSessionRecord> {
        val request = ReadRecordsRequest(
            recordType = ExerciseSessionRecord::class,
            timeRangeFilter = TimeRangeFilter.between(start, end)
        )
        val response = healthConnectClient.readRecords(request = request)
        return response.records
    }

    /**
     * 오른층계기록
     */
    suspend fun readFloorsClimbedRecord(start: Instant, end: Instant): List<FloorsClimbedRecord> {
        val request = ReadRecordsRequest(
            recordType = FloorsClimbedRecord::class,
            timeRangeFilter = TimeRangeFilter.between(start, end)
        )
        val response = healthConnectClient.readRecords(request = request)
        return response.records
    }

    /**
     * 수분기록
     */
    suspend fun readHydrationRecord(start: Instant, end: Instant): List<HydrationRecord> {
        val request = ReadRecordsRequest(
            recordType = HydrationRecord::class,
            timeRangeFilter = TimeRangeFilter.between(start, end)
        )
        val response = healthConnectClient.readRecords(request = request)
        return response.records
    }

    /**
     * 심박수변동기록
     */
    suspend fun readHeartRateVariabilityRmssdRecord(start: Instant, end: Instant): List<HeartRateVariabilityRmssdRecord> {
        val request = ReadRecordsRequest(
            recordType = HeartRateVariabilityRmssdRecord::class,
            timeRangeFilter = TimeRangeFilter.between(start, end)
        )
        val response = healthConnectClient.readRecords(request = request)
        return response.records
    }

    /**
     * 높이기록
     */
    suspend fun readHeightRecord(start: Instant, end: Instant): List<HeightRecord> {
        val request = ReadRecordsRequest(
            recordType = HeightRecord::class,
            timeRangeFilter = TimeRangeFilter.between(start, end)
        )
        val response = healthConnectClient.readRecords(request = request)
        return response.records
    }

    /**
     * 제지방량기록
     */
    suspend fun readLeanBodyMassRecord(start: Instant, end: Instant): List<LeanBodyMassRecord> {
        val request = ReadRecordsRequest(
            recordType = LeanBodyMassRecord::class,
            timeRangeFilter = TimeRangeFilter.between(start, end)
        )
        val response = healthConnectClient.readRecords(request = request)
        return response.records
    }

    /**
     * 영양기록
     */
    suspend fun readNutritionRecord(start: Instant, end: Instant): List<NutritionRecord> {
        val request = ReadRecordsRequest(
            recordType = NutritionRecord::class,
            timeRangeFilter = TimeRangeFilter.between(start, end)
        )
        val response = healthConnectClient.readRecords(request = request)
        return response.records
    }

    /**
     * 산소포화도기록
     */
    suspend fun readOxygenSaturationRecord(start: Instant, end: Instant): List<OxygenSaturationRecord> {
        val request = ReadRecordsRequest(
            recordType = OxygenSaturationRecord::class,
            timeRangeFilter = TimeRangeFilter.between(start, end)
        )
        val response = healthConnectClient.readRecords(request = request)
        return response.records
    }

    /**
     * 힘기록
     */
    suspend fun readPowerRecord(start: Instant, end: Instant): List<PowerRecord> {
        val request = ReadRecordsRequest(
            recordType = PowerRecord::class,
            timeRangeFilter = TimeRangeFilter.between(start, end)
        )
        val response = healthConnectClient.readRecords(request = request)
        return response.records
    }

    /**
     * 호흡수기록
     */
    suspend fun readRespiratoryRateRecord(start: Instant, end: Instant): List<RespiratoryRateRecord> {
        val request = ReadRecordsRequest(
            recordType = RespiratoryRateRecord::class,
            timeRangeFilter = TimeRangeFilter.between(start, end)
        )
        val response = healthConnectClient.readRecords(request = request)
        return response.records
    }

    /**
     * 안정시 심박수 기록
     */
    suspend fun readRestingHeartRateRecord(start: Instant, end: Instant): List<RestingHeartRateRecord> {
        val request = ReadRecordsRequest(
            recordType = RestingHeartRateRecord::class,
            timeRangeFilter = TimeRangeFilter.between(start, end)
        )
        val response = healthConnectClient.readRecords(request = request)
        return response.records
    }

    /**
     * 수면세션기록
     */
    suspend fun readSleepSessionRecord(start: Instant, end: Instant): List<SleepSessionRecord> {
        val request = ReadRecordsRequest(
            recordType = SleepSessionRecord::class,
            timeRangeFilter = TimeRangeFilter.between(start, end)
        )
        val response = healthConnectClient.readRecords(request = request)
        return response.records
    }

    /**
     * 속도기록
     */
    suspend fun readSpeedRecord(start: Instant, end: Instant): List<SpeedRecord> {
        val request = ReadRecordsRequest(
            recordType = SpeedRecord::class,
            timeRangeFilter = TimeRangeFilter.between(start, end)
        )
        val response = healthConnectClient.readRecords(request = request)
        return response.records
    }

    /**
     * 걸음수 케이던스 기록
     */
    suspend fun readStepsCadenceRecord(start: Instant, end: Instant): List<StepsCadenceRecord> {
        val request = ReadRecordsRequest(
            recordType = StepsCadenceRecord::class,
            timeRangeFilter = TimeRangeFilter.between(start, end)
        )
        val response = healthConnectClient.readRecords(request = request)
        return response.records
    }

    /**
     * 걸음수기록
     */
    suspend fun readStepsRecord(start: Instant, end: Instant): List<StepsRecord> {
        val request = ReadRecordsRequest(
            recordType = StepsRecord::class,
            timeRangeFilter = TimeRangeFilter.between(start, end)
        )
        val response = healthConnectClient.readRecords(request = request)
        return response.records
    }

    /**
     * 체중기록
     */
    suspend fun readWeightRecord(start: Instant, end: Instant): List<WeightRecord> {
        val request = ReadRecordsRequest(
            recordType = WeightRecord::class,
            timeRangeFilter = TimeRangeFilter.between(start, end)
        )
        val response = healthConnectClient.readRecords(request = request)
        return response.records
    }
}

enum class HealthConnectAvailability {
    INSTALLED,
    NOT_INSTALLED,
    NOT_SUPPORTED
}