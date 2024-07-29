package com.example.healthconnectdev.presentation.screen.onboard

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.ActiveCaloriesBurnedRecord
import androidx.health.connect.client.records.BasalBodyTemperatureRecord
import androidx.health.connect.client.records.BasalMetabolicRateRecord
import androidx.health.connect.client.records.BloodGlucoseRecord
import androidx.health.connect.client.records.BloodPressureRecord
import androidx.health.connect.client.records.BodyTemperatureMeasurementLocation
import androidx.health.connect.client.records.CyclingPedalingCadenceRecord
import androidx.health.connect.client.records.DistanceRecord
import androidx.health.connect.client.records.ElevationGainedRecord
import androidx.health.connect.client.records.ExerciseSessionRecord
import androidx.health.connect.client.records.FloorsClimbedRecord
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.HeightRecord
import androidx.health.connect.client.records.HydrationRecord
import androidx.health.connect.client.records.NutritionRecord
import androidx.health.connect.client.records.PowerRecord
import androidx.health.connect.client.records.RestingHeartRateRecord
import androidx.health.connect.client.records.SleepSessionRecord
import androidx.health.connect.client.records.SpeedRecord
import androidx.health.connect.client.records.StepsCadenceRecord
import androidx.health.connect.client.records.StepsRecord
import androidx.health.connect.client.records.TotalCaloriesBurnedRecord
import androidx.health.connect.client.records.WeightRecord
import androidx.health.connect.client.request.AggregateRequest
import androidx.health.connect.client.time.TimeRangeFilter
import androidx.health.connect.client.units.Temperature
import androidx.lifecycle.ViewModel
import com.example.healthconnectdev.data.HealthConnectManager
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.time.Instant

class OnboardingViewModel(
    private val healthConnectManager: HealthConnectManager,
    private val healthConnectClient: HealthConnectClient
) : ViewModel() {

    private var _heartRate = MutableSharedFlow<List<HeartRateRecord>>()
    val heartRate = _heartRate.asSharedFlow()

    fun getHealthConnectPermissions(): Set<String> {
        val permission = setOf(
            HealthPermission.getReadPermission(HeartRateRecord::class),
            HealthPermission.getReadPermission(StepsRecord::class),
            HealthPermission.getReadPermission(ExerciseSessionRecord::class),
            HealthPermission.getReadPermission(TotalCaloriesBurnedRecord::class)
        )
        return permission
    }

    suspend fun readHeartRateSessionData() {
        val heartRateRecord = healthConnectManager.readHeartRate(Instant.now().minusSeconds(60 * 60 * 24), Instant.now())
        val uid = heartRateRecord.first().metadata.id
        val heartRateSession = healthConnectClient.readRecord(HeartRateRecord::class, uid)
        val timeRangeFilter = TimeRangeFilter.between(Instant.now().minusSeconds(60 * 60 * 24), Instant.now())
        val aggregateDataTypes = setOf(
            HeartRateRecord.BPM_AVG,
            HeartRateRecord.BPM_MIN,
            HeartRateRecord.BPM_MAX,
            HeartRateRecord.MEASUREMENTS_COUNT,
        )
        val dataOriginFilter = setOf(heartRateSession.record.metadata.dataOrigin)
        val aggregateRequest = AggregateRequest(
            metrics = aggregateDataTypes,
            timeRangeFilter = timeRangeFilter,
            dataOriginFilter = dataOriginFilter
        )
        val aggregateData = healthConnectClient.aggregate(aggregateRequest)
        Log.e("OnboardingViewModel", "Heart Rate Session Data - BPM_AVG: ${aggregateData[HeartRateRecord.BPM_AVG]}")
        Log.e("OnboardingViewModel", "Heart Rate Session Data - BPM_MIN: ${aggregateData[HeartRateRecord.BPM_MIN]}")
        Log.e("OnboardingViewModel", "Heart Rate Session Data - BPM_MAX: ${aggregateData[HeartRateRecord.BPM_MAX]}")
        Log.e("OnboardingViewModel", "Heart Rate Session Data - MEASUREMENTS_COUNT: ${aggregateData[HeartRateRecord.MEASUREMENTS_COUNT]}")
    }

    suspend fun readActiveCaloriesBurnedSessionData() {
        val activeCaloriesBurnedRecord = healthConnectManager.readActiveCaloriesBurnedRecord(Instant.now().minusSeconds(60 * 60 * 24), Instant.now())
        val uid = activeCaloriesBurnedRecord.first().metadata.id
        val activeCaloriesBurnedSession = healthConnectClient.readRecord(ActiveCaloriesBurnedRecord::class, uid)
        val timeRangeFilter = TimeRangeFilter.between(Instant.now().minusSeconds(60 * 60 * 24), Instant.now())
        val aggregateDataTypes = setOf(
            ActiveCaloriesBurnedRecord.ACTIVE_CALORIES_TOTAL
        )
        val dataOriginFilter = setOf(activeCaloriesBurnedSession.record.metadata.dataOrigin)
        val aggregateRequest = AggregateRequest(
            metrics = aggregateDataTypes,
            timeRangeFilter = timeRangeFilter,
            dataOriginFilter = dataOriginFilter
        )
        val aggregateData = healthConnectClient.aggregate(aggregateRequest)
        Log.e("OnboardingViewModel", "Active Calories Burned Session Data - ACTIVE_CALORIES_TOTAL: ${aggregateData[ActiveCaloriesBurnedRecord.ACTIVE_CALORIES_TOTAL]}")
    }

    suspend fun readBasalBodyTemperatureSessionData() {
        val basalBodyTemperatureRecord = healthConnectManager.readBasalBodyTemperatureRecord(Instant.now().minusSeconds(60 * 60 * 24), Instant.now())
        val uid = basalBodyTemperatureRecord.first().metadata.id
        val basalBodyTemperatureSession = healthConnectClient.readRecord(BasalBodyTemperatureRecord::class, uid)
        Log.e("OnboardingViewModel", "Basal Body Temperature Session Data - Temperature: ${basalBodyTemperatureSession.record.temperature}")
        Log.e("OnboardingViewModel", "Basal Body Temperature Session Data - Measurement Location: ${basalBodyTemperatureSession.record.measurementLocation}")
    }

    suspend fun readBasalMetabolicRateSessionData() {
        val basalMetabolicRateRecord = healthConnectManager.readBasalMetabolicRateRecord(Instant.now().minusSeconds(60 * 60 * 24), Instant.now())
        val uid = basalMetabolicRateRecord.first().metadata.id
        val basalMetabolicRateSession = healthConnectClient.readRecord(BasalMetabolicRateRecord::class, uid)
        val timeRangeFilter = TimeRangeFilter.between(Instant.now().minusSeconds(60 * 60 * 24), Instant.now())
        val aggregateDataTypes = setOf(
            BasalMetabolicRateRecord.BASAL_CALORIES_TOTAL
        )
        val dataOriginFilter = setOf(basalMetabolicRateSession.record.metadata.dataOrigin)
        val aggregateRequest = AggregateRequest(
            metrics = aggregateDataTypes,
            timeRangeFilter = timeRangeFilter,
            dataOriginFilter = dataOriginFilter
        )
        val aggregateData = healthConnectClient.aggregate(aggregateRequest)
        Log.e("OnboardingViewModel", "Basal Metabolic Rate Session Data - BASAL_CALORIES_TOTAL: ${aggregateData[BasalMetabolicRateRecord.BASAL_CALORIES_TOTAL]}")
    }

    @SuppressLint("RestrictedApi")
    suspend fun readBloodGlucoseSessionData() {
        val bloodGlucoseRecord = healthConnectManager.readBloodGlucoseRecord(Instant.now().minusSeconds(60 * 60 * 24), Instant.now())
        bloodGlucoseRecord.forEach {
            Log.e("OnboardingViewModel", "Blood Glucose Session Data - Blood Glucose Session id: ${it.metadata.id}")
            Log.e("OnboardingViewModel", "Blood Glucose Session Data - Blood Glucose Session dataOrigin: ${it.metadata.clientRecordId}")
            Log.e("OnboardingViewModel", "Blood Glucose Session Data - Blood Glucose Session dataOrigin: ${it.metadata.dataOrigin.packageName}")
            Log.e("OnboardingViewModel", "Blood Glucose Session Data - Blood Glucose Session dataOrigin: ${it.metadata.device?.model}")
            Log.e("OnboardingViewModel", "Blood Glucose Session Data - Blood Glucose Session Time: ${it.time}")
            Log.e("OnboardingViewModel", "Blood Glucose Session Data - Blood Glucose Session Level: ${it.level}")
            Log.e("OnboardingViewModel", "Blood Glucose Session Data - Blood Glucose Session Meal: ${it.relationToMeal}")
            Log.e("OnboardingViewModel", "Blood Glucose Session Data - Blood Glucose Session Meal Type: ${it.mealType}")
        }
    }

    suspend fun readBloodPressureSessionData() {
        val bloodPressureRecord = healthConnectManager.readBloodPressureRecord(Instant.now().minusSeconds(60 * 60 * 24), Instant.now())
        val uid = bloodPressureRecord.first().metadata.id
        val bloodPressureSession = healthConnectClient.readRecord(BloodPressureRecord::class, uid)
        val timeRangeFilter = TimeRangeFilter.between(Instant.now().minusSeconds(60 * 60 * 24), Instant.now())
        val aggregateDataTypes = setOf(
            BloodPressureRecord.DIASTOLIC_AVG,
            BloodPressureRecord.DIASTOLIC_MIN,
            BloodPressureRecord.DIASTOLIC_MAX,
            BloodPressureRecord.SYSTOLIC_AVG,
            BloodPressureRecord.SYSTOLIC_MIN,
            BloodPressureRecord.SYSTOLIC_MAX,
        )
        val dataOriginFilter = setOf(bloodPressureSession.record.metadata.dataOrigin)
        val aggregateRequest = AggregateRequest(
            metrics = aggregateDataTypes,
            timeRangeFilter = timeRangeFilter,
            dataOriginFilter = dataOriginFilter
        )
        val aggregateData = healthConnectClient.aggregate(aggregateRequest)

        Log.e("OnboardingViewModel", "Blood Pressure Session Data - DIASTOLIC_AVG: ${aggregateData[BloodPressureRecord.DIASTOLIC_AVG]?.inMillimetersOfMercury}")
        Log.e("OnboardingViewModel", "Blood Pressure Session Data - DIASTOLIC_MIN: ${aggregateData[BloodPressureRecord.DIASTOLIC_MIN]}")
        Log.e("OnboardingViewModel", "Blood Pressure Session Data - DIASTOLIC_MAX: ${aggregateData[BloodPressureRecord.DIASTOLIC_MAX]}")
        Log.e("OnboardingViewModel", "Blood Pressure Session Data - SYSTOLIC_AVG: ${aggregateData[BloodPressureRecord.SYSTOLIC_AVG]}")
        Log.e("OnboardingViewModel", "Blood Pressure Session Data - SYSTOLIC_MIN: ${aggregateData[BloodPressureRecord.SYSTOLIC_MIN]}")
        Log.e("OnboardingViewModel", "Blood Pressure Session Data - SYSTOLIC_MAX: ${aggregateData[BloodPressureRecord.SYSTOLIC_MAX]}")
    }

    suspend fun readBodyFatSessionData() {
        val readBodyFatRecord = healthConnectManager.readBodyFatRecord(Instant.now().minusSeconds(60 * 60 * 24), Instant.now())
        readBodyFatRecord.forEach {
            Log.e("OnboardingViewModel", "Body Fat Session Data - Body Fat Session percentage: ${it.percentage}")
        }
    }

    suspend fun readBodyTemperatureSessionData() {
        val readBodyTemperatureRecord = healthConnectManager.readBodyTemperatureRecord(Instant.now().minusSeconds(60 * 60 * 24), Instant.now())
        readBodyTemperatureRecord.forEach {
            Log.e("OnboardingViewModel", "Body Temperature Session Data - Body Temperature Session temperature: ${it.temperature}")
            Log.e("OnboardingViewModel", "Body Temperature Session Data - Body Temperature Session location: ${it.measurementLocation}")
        }
    }

    suspend fun readBodyWaterMassSessionData() {
        val readBodyWaterMassRecord = healthConnectManager.readBodyWaterMassRecord(Instant.now().minusSeconds(60 * 60 * 24), Instant.now())
        readBodyWaterMassRecord.forEach {
            Log.e("OnboardingViewModel", "Body Water Mass Session Data - Body Water Mass Session mass: ${it.mass}")
        }
    }

    suspend fun readBoneMassSessionData() {
        val readBoneMassRecord = healthConnectManager.readBoneMassRecord(Instant.now().minusSeconds(60 * 60 * 24), Instant.now())
        readBoneMassRecord.forEach {
            Log.e("OnboardingViewModel", "Bone Mass Session Data - Bone Mass Session mass: ${it.mass}")
        }
    }

    suspend fun readCervicalMucusSessionData() {
        val readCervicalMucusRecord = healthConnectManager.readCervicalMucusRecord(Instant.now().minusSeconds(60 * 60 * 24), Instant.now())
        readCervicalMucusRecord.forEach {
            Log.e("OnboardingViewModel", "Cervical Mucus Session Data - Cervical Mucus Session sensation: ${it.sensation}")
            Log.e("OnboardingViewModel", "Cervical Mucus Session Data - Cervical Mucus Session appearance: ${it.appearance}")
        }
    }

    suspend fun readCyclingPedalingCadenceSessionData() {
        val readCyclingPedalingCadenceRecord = healthConnectManager.readCyclingPedalingCadenceRecord(Instant.now().minusSeconds(60 * 60 * 24), Instant.now())
        val uid = readCyclingPedalingCadenceRecord.first().metadata.id
        val cyclingPedalingCadenceSession = healthConnectClient.readRecord(CyclingPedalingCadenceRecord::class, uid)
        val timeRangeFilter = TimeRangeFilter.between(Instant.now().minusSeconds(60 * 60 * 24), Instant.now())
        val aggregateDataTypes = setOf(
            CyclingPedalingCadenceRecord.RPM_AVG,
            CyclingPedalingCadenceRecord.RPM_MAX,
            CyclingPedalingCadenceRecord.RPM_MIN,
        )
        val dataOriginFilter = setOf(cyclingPedalingCadenceSession.record.metadata.dataOrigin)
        val aggregateRequest = AggregateRequest(
            metrics = aggregateDataTypes,
            timeRangeFilter = timeRangeFilter,
            dataOriginFilter = dataOriginFilter
        )
        val aggregateData = healthConnectClient.aggregate(aggregateRequest)
        Log.e("OnboardingViewModel", "Cycling Pedaling Cadence Session Data - RPM_AVG: ${aggregateData[CyclingPedalingCadenceRecord.RPM_AVG]}")
        Log.e("OnboardingViewModel", "Cycling Pedaling Cadence Session Data - RPM_MAX: ${aggregateData[CyclingPedalingCadenceRecord.RPM_MAX]}")
        Log.e("OnboardingViewModel", "Cycling Pedaling Cadence Session Data - RPM_MIN: ${aggregateData[CyclingPedalingCadenceRecord.RPM_MIN]}")
    }

    suspend fun readDistanceSessionData() {
        val distanceRecord = healthConnectManager.readDistanceRecord(Instant.now().minusSeconds(60 * 60 * 24), Instant.now())
        val uid = distanceRecord.first().metadata.id
        val distanceSession = healthConnectClient.readRecord(DistanceRecord::class, uid)
        val timeRangeFilter = TimeRangeFilter.between(Instant.now().minusSeconds(60 * 60 * 24), Instant.now())
        val aggregateDataTypes = setOf(
            DistanceRecord.DISTANCE_TOTAL
        )
        val dataOriginFilter = setOf(distanceSession.record.metadata.dataOrigin)
        val aggregateRequest = AggregateRequest(
            metrics = aggregateDataTypes,
            timeRangeFilter = timeRangeFilter,
            dataOriginFilter = dataOriginFilter
        )
        val aggregateData = healthConnectClient.aggregate(aggregateRequest)
        Log.e("OnboardingViewModel", "Distance Session Data - DISTANCE_TOTAL: ${aggregateData[DistanceRecord.DISTANCE_TOTAL]}")
    }

    suspend fun readElevationGainedSessionData() {
        val elevationGainedRecord = healthConnectManager.readElevationGainedRecord(Instant.now().minusSeconds(60 * 60 * 24), Instant.now())
        val uid = elevationGainedRecord.first().metadata.id
        val elevationGainedSession = healthConnectClient.readRecord(ElevationGainedRecord::class, uid)
        val timeRangeFilter = TimeRangeFilter.between(Instant.now().minusSeconds(60 * 60 * 24), Instant.now())
        val aggregateDataTypes = setOf(
            ElevationGainedRecord.ELEVATION_GAINED_TOTAL
        )
        val dataOriginFilter = setOf(elevationGainedSession.record.metadata.dataOrigin)
        val aggregateRequest = AggregateRequest(
            metrics = aggregateDataTypes,
            timeRangeFilter = timeRangeFilter,
            dataOriginFilter = dataOriginFilter
        )
        val aggregateData = healthConnectClient.aggregate(aggregateRequest)
        Log.e("OnboardingViewModel", "Elevation Gained Session Data - ELEVATION_GAINED_TOTAL: ${aggregateData[ElevationGainedRecord.ELEVATION_GAINED_TOTAL]}")
    }

    suspend fun readExerciseSessionData() {
        val exerciseSessionRecord = healthConnectManager.readExerciseSessionRecord(Instant.now().minusSeconds(60 * 60 * 24), Instant.now())
        exerciseSessionRecord.forEach {
            Log.e("OnboardingViewModel", "Exercise Session Data - Exercise Session exerciseType: ${it.exerciseType}")
            Log.e("OnboardingViewModel", "Exercise Session Data - Exercise Session notes: ${it.notes}")
            Log.e("OnboardingViewModel", "Exercise Session Data - Exercise Session title: ${it.title}")
        }
        val uid = exerciseSessionRecord.first().metadata.id
        val exerciseSession = healthConnectClient.readRecord(ExerciseSessionRecord::class, uid)
        val timeRangeFilter = TimeRangeFilter.between(Instant.now().minusSeconds(60 * 60 * 24), Instant.now())
        val aggregateDataTypes = setOf(
            ExerciseSessionRecord.EXERCISE_DURATION_TOTAL
        )
        val dataOriginFilter = setOf(exerciseSession.record.metadata.dataOrigin)
        val aggregateRequest = AggregateRequest(
            metrics = aggregateDataTypes,
            timeRangeFilter = timeRangeFilter,
            dataOriginFilter = dataOriginFilter
        )
        val aggregateData = healthConnectClient.aggregate(aggregateRequest)
        Log.e("OnboardingViewModel", "Exercise Session Data - EXERCISE_DURATION_TOTAL: ${aggregateData[ExerciseSessionRecord.EXERCISE_DURATION_TOTAL]}")
    }

    suspend fun readFloorsClimbedSessionData() {
        val floorsClimbedRecord = healthConnectManager.readFloorsClimbedRecord(Instant.now().minusSeconds(60 * 60 * 24), Instant.now())
        val uid = floorsClimbedRecord.first().metadata.id
        val floorsClimbedSession = healthConnectClient.readRecord(FloorsClimbedRecord::class, uid)
        val timeRangeFilter = TimeRangeFilter.between(Instant.now().minusSeconds(60 * 60 * 24), Instant.now())
        val aggregateDataTypes = setOf(
            FloorsClimbedRecord.FLOORS_CLIMBED_TOTAL
        )
        val dataOriginFilter = setOf(floorsClimbedSession.record.metadata.dataOrigin)
        val aggregateRequest = AggregateRequest(
            metrics = aggregateDataTypes,
            timeRangeFilter = timeRangeFilter,
            dataOriginFilter = dataOriginFilter
        )
        val aggregateData = healthConnectClient.aggregate(aggregateRequest)
        Log.e("OnboardingViewModel", "Floors Climbed Session Data - FLOORS_CLIMBED_TOTAL: ${aggregateData[FloorsClimbedRecord.FLOORS_CLIMBED_TOTAL]}")
    }

    suspend fun readHydrationSessionData() {
        val hydrationRecord = healthConnectManager.readHydrationRecord(Instant.now().minusSeconds(60 * 60 * 24), Instant.now())
        val uid = hydrationRecord.first().metadata.id
        val hydrationSession = healthConnectClient.readRecord(HydrationRecord::class, uid)
        val timeRangeFilter = TimeRangeFilter.between(Instant.now().minusSeconds(60 * 60 * 24), Instant.now())
        val aggregateDataTypes = setOf(
            HydrationRecord.VOLUME_TOTAL
        )
        val dataOriginFilter = setOf(hydrationSession.record.metadata.dataOrigin)
        val aggregateRequest = AggregateRequest(
            metrics = aggregateDataTypes,
            timeRangeFilter = timeRangeFilter,
            dataOriginFilter = dataOriginFilter
        )
        val aggregateData = healthConnectClient.aggregate(aggregateRequest)
        Log.e("OnboardingViewModel", "Hydration Session Data - HYDRATION_TOTAL: ${aggregateData[HydrationRecord.VOLUME_TOTAL]}")
    }

    suspend fun readHeartRateVariabilityRmssdSessionData() {
        val heartRateVariabilityRmssdRecord = healthConnectManager.readHeartRateVariabilityRmssdRecord(Instant.now().minusSeconds(60 * 60 * 24), Instant.now())
        heartRateVariabilityRmssdRecord.forEach {
            Log.e("OnboardingViewModel", "Heart Rate Variability Rmssd Session Data - Heart Rate Variability Rmssd Session rmssd: ${it.heartRateVariabilityMillis}")
        }
    }

    suspend fun readHeightSessionData() {
        val heightRecord = healthConnectManager.readHeightRecord(Instant.now().minusSeconds(60 * 60 * 24), Instant.now())
        val uid = heightRecord.first().metadata.id
        val heightSession = healthConnectClient.readRecord(HeightRecord::class, uid)
        val timeRangeFilter = TimeRangeFilter.between(Instant.now().minusSeconds(60 * 60 * 24), Instant.now())
        val aggregateDataTypes = setOf(
            HeightRecord.HEIGHT_AVG,
            HeightRecord.HEIGHT_MIN,
            HeightRecord.HEIGHT_MAX,
        )
        val dataOriginFilter = setOf(heightSession.record.metadata.dataOrigin)
        val aggregateRequest = AggregateRequest(
            metrics = aggregateDataTypes,
            timeRangeFilter = timeRangeFilter,
            dataOriginFilter = dataOriginFilter
        )
        val aggregateData = healthConnectClient.aggregate(aggregateRequest)
        Log.e("OnboardingViewModel", "Height Session Data - HEIGHT_AVG: ${aggregateData[HeightRecord.HEIGHT_AVG]}")
        Log.e("OnboardingViewModel", "Height Session Data - HEIGHT_MIN: ${aggregateData[HeightRecord.HEIGHT_MIN]}")
        Log.e("OnboardingViewModel", "Height Session Data - HEIGHT_MAX: ${aggregateData[HeightRecord.HEIGHT_MAX]}")
    }

    suspend fun readLeanBodyMassSessionData() {
        val leanBodyMassRecord = healthConnectManager.readLeanBodyMassRecord(Instant.now().minusSeconds(60 * 60 * 24), Instant.now())
        leanBodyMassRecord.forEach {
            Log.e("OnboardingViewModel", "Lean Body Mass Session Data - Lean Body Mass Session mass: ${it.mass}")
        }
    }

    suspend fun readNutritionSessionData() {
        val nutritionRecord = healthConnectManager.readNutritionRecord(Instant.now().minusSeconds(60 * 60 * 24), Instant.now())
        val uid = nutritionRecord.first().metadata.id
        val nutritionSession = healthConnectClient.readRecord(NutritionRecord::class, uid)
        val timeRangeFilter = TimeRangeFilter.between(Instant.now().minusSeconds(60 * 60 * 24), Instant.now())
        val aggregateDataTypes = setOf(
            NutritionRecord.BIOTIN_TOTAL,
            NutritionRecord.CAFFEINE_TOTAL,
            NutritionRecord.CALCIUM_TOTAL,
            NutritionRecord.CHOLESTEROL_TOTAL,
            NutritionRecord.CHROMIUM_TOTAL,
            NutritionRecord.ENERGY_TOTAL,
            NutritionRecord.ENERGY_FROM_FAT_TOTAL,
            NutritionRecord.FOLATE_TOTAL,
            NutritionRecord.IODINE_TOTAL,
            NutritionRecord.IRON_TOTAL,
            NutritionRecord.MANGANESE_TOTAL,
            NutritionRecord.MONOUNSATURATED_FAT_TOTAL,
            NutritionRecord.NIACIN_TOTAL,
            NutritionRecord.PANTOTHENIC_ACID_TOTAL,
            NutritionRecord.PHOSPHORUS_TOTAL,
            NutritionRecord.POTASSIUM_TOTAL,
            NutritionRecord.RIBOFLAVIN_TOTAL,
            NutritionRecord.SATURATED_FAT_TOTAL,
            NutritionRecord.SELENIUM_TOTAL,
            NutritionRecord.SODIUM_TOTAL,
            NutritionRecord.SUGAR_TOTAL,
            NutritionRecord.TOTAL_CARBOHYDRATE_TOTAL,
            NutritionRecord.TOTAL_FAT_TOTAL,
            NutritionRecord.UNSATURATED_FAT_TOTAL,
            NutritionRecord.VITAMIN_A_TOTAL,
            NutritionRecord.VITAMIN_B12_TOTAL,
            NutritionRecord.VITAMIN_B6_TOTAL,
            NutritionRecord.VITAMIN_C_TOTAL,
            NutritionRecord.VITAMIN_D_TOTAL,
            NutritionRecord.VITAMIN_E_TOTAL,
            NutritionRecord.VITAMIN_K_TOTAL,
            NutritionRecord.ZINC_TOTAL
        )
        val dataOriginFilter = setOf(nutritionSession.record.metadata.dataOrigin)
        val aggregateRequest = AggregateRequest(
            metrics = aggregateDataTypes,
            timeRangeFilter = timeRangeFilter,
            dataOriginFilter = dataOriginFilter
        )
        val aggregateData = healthConnectClient.aggregate(aggregateRequest)
        Log.e("OnboardingViewModel", "Nutrition Session Data - BIOTIN_TOTAL: ${aggregateData[NutritionRecord.BIOTIN_TOTAL]}")
        Log.e("OnboardingViewModel", "Nutrition Session Data - CAFFEINE_TOTAL: ${aggregateData[NutritionRecord.CAFFEINE_TOTAL]}")
        Log.e("OnboardingViewModel", "Nutrition Session Data - CALCIUM_TOTAL: ${aggregateData[NutritionRecord.CALCIUM_TOTAL]}")
        Log.e("OnboardingViewModel", "Nutrition Session Data - CHOLESTEROL_TOTAL: ${aggregateData[NutritionRecord.CHOLESTEROL_TOTAL]}")
        Log.e("OnboardingViewModel", "Nutrition Session Data - CHROMIUM_TOTAL: ${aggregateData[NutritionRecord.CHROMIUM_TOTAL]}")
        Log.e("OnboardingViewModel", "Nutrition Session Data - ENERGY_TOTAL: ${aggregateData[NutritionRecord.ENERGY_TOTAL]}")
        Log.e("OnboardingViewModel", "Nutrition Session Data - ENERGY_FROM_FAT_TOTAL: ${aggregateData[NutritionRecord.ENERGY_FROM_FAT_TOTAL]}")
        Log.e("OnboardingViewModel", "Nutrition Session Data - FOLATE_TOTAL: ${aggregateData[NutritionRecord.FOLATE_TOTAL]}")
        Log.e("OnboardingViewModel", "Nutrition Session Data - IODINE_TOTAL: ${aggregateData[NutritionRecord.IODINE_TOTAL]}")
        Log.e("OnboardingViewModel", "Nutrition Session Data - IRON_TOTAL: ${aggregateData[NutritionRecord.IRON_TOTAL]}")
        Log.e("OnboardingViewModel", "Nutrition Session Data - MANGANESE_TOTAL: ${aggregateData[NutritionRecord.MANGANESE_TOTAL]}")
        Log.e("OnboardingViewModel", "Nutrition Session Data - MONOUNSATURATED_FAT_TOTAL: ${aggregateData[NutritionRecord.MONOUNSATURATED_FAT_TOTAL]}")
        Log.e("OnboardingViewModel", "Nutrition Session Data - NIACIN_TOTAL: ${aggregateData[NutritionRecord.NIACIN_TOTAL]}")
        Log.e("OnboardingViewModel", "Nutrition Session Data - PANTOTHENIC_ACID_TOTAL: ${aggregateData[NutritionRecord.PANTOTHENIC_ACID_TOTAL]}")
        Log.e("OnboardingViewModel", "Nutrition Session Data - PHOSPHORUS_TOTAL: ${aggregateData[NutritionRecord.PHOSPHORUS_TOTAL]}")
        Log.e("OnboardingViewModel", "Nutrition Session Data - POTASSIUM_TOTAL: ${aggregateData[NutritionRecord.POTASSIUM_TOTAL]}")
        Log.e("OnboardingViewModel", "Nutrition Session Data - RIBOFLAVIN_TOTAL: ${aggregateData[NutritionRecord.RIBOFLAVIN_TOTAL]}")
        Log.e("OnboardingViewModel", "Nutrition Session Data - SATURATED_FAT_TOTAL: ${aggregateData[NutritionRecord.SATURATED_FAT_TOTAL]}")
        Log.e("OnboardingViewModel", "Nutrition Session Data - SELENIUM_TOTAL: ${aggregateData[NutritionRecord.SELENIUM_TOTAL]}")
        Log.e("OnboardingViewModel", "Nutrition Session Data - SODIUM_TOTAL: ${aggregateData[NutritionRecord.SODIUM_TOTAL]}")
        Log.e("OnboardingViewModel", "Nutrition Session Data - SUGAR_TOTAL: ${aggregateData[NutritionRecord.SUGAR_TOTAL]}")
        Log.e("OnboardingViewModel", "Nutrition Session Data - TOTAL_CARBOHYDRATE_TOTAL: ${aggregateData[NutritionRecord.TOTAL_CARBOHYDRATE_TOTAL]}")
        Log.e("OnboardingViewModel", "Nutrition Session Data - TOTAL_FAT_TOTAL: ${aggregateData[NutritionRecord.TOTAL_FAT_TOTAL]}")
        Log.e("OnboardingViewModel", "Nutrition Session Data - UNSATURATED_FAT_TOTAL: ${aggregateData[NutritionRecord.UNSATURATED_FAT_TOTAL]}")
        Log.e("OnboardingViewModel", "Nutrition Session Data - VITAMIN_A_TOTAL: ${aggregateData[NutritionRecord.VITAMIN_A_TOTAL]}")
        Log.e("OnboardingViewModel", "Nutrition Session Data - VITAMIN_B12_TOTAL: ${aggregateData[NutritionRecord.VITAMIN_B12_TOTAL]}")
        Log.e("OnboardingViewModel", "Nutrition Session Data - VITAMIN_B6_TOTAL: ${aggregateData[NutritionRecord.VITAMIN_B6_TOTAL]}")
        Log.e("OnboardingViewModel", "Nutrition Session Data - VITAMIN_C_TOTAL: ${aggregateData[NutritionRecord.VITAMIN_C_TOTAL]}")
        Log.e("OnboardingViewModel", "Nutrition Session Data - VITAMIN_D_TOTAL: ${aggregateData[NutritionRecord.VITAMIN_D_TOTAL]}")
        Log.e("OnboardingViewModel", "Nutrition Session Data - VITAMIN_E_TOTAL: ${aggregateData[NutritionRecord.VITAMIN_E_TOTAL]}")
        Log.e("OnboardingViewModel", "Nutrition Session Data - VITAMIN_K_TOTAL: ${aggregateData[NutritionRecord.VITAMIN_K_TOTAL]}")
        Log.e("OnboardingViewModel", "Nutrition Session Data - ZINC_TOTAL: ${aggregateData[NutritionRecord.ZINC_TOTAL]}")
    }

    suspend fun readOxygenSaturationSessionData() {
        val oxygenSaturationRecord = healthConnectManager.readOxygenSaturationRecord(Instant.now().minusSeconds(60 * 60 * 24), Instant.now())
        oxygenSaturationRecord.forEach {
            Log.e("OnboardingViewModel", "Oxygen Saturation Session Data - Oxygen Saturation Session percentage: ${it.percentage}")
        }
    }

    suspend fun readPowerSessionData() {
        val powerRecord = healthConnectManager.readPowerRecord(Instant.now().minusSeconds(60 * 60 * 24), Instant.now())
        val uid = powerRecord.first().metadata.id
        val powerSession = healthConnectClient.readRecord(PowerRecord::class, uid)
        val timeRangeFilter = TimeRangeFilter.between(Instant.now().minusSeconds(60 * 60 * 24), Instant.now())
        val aggregateDataTypes = setOf(
            PowerRecord.POWER_AVG,
            PowerRecord.POWER_MIN,
            PowerRecord.POWER_MAX,
        )
        val dataOriginFilter = setOf(powerSession.record.metadata.dataOrigin)
        val aggregateRequest = AggregateRequest(
            metrics = aggregateDataTypes,
            timeRangeFilter = timeRangeFilter,
            dataOriginFilter = dataOriginFilter
        )
        val aggregateData = healthConnectClient.aggregate(aggregateRequest)
        Log.e("OnboardingViewModel", "Power Session Data - POWER_AVG: ${aggregateData[PowerRecord.POWER_AVG]}")
        Log.e("OnboardingViewModel", "Power Session Data - POWER_MIN: ${aggregateData[PowerRecord.POWER_MIN]}")
        Log.e("OnboardingViewModel", "Power Session Data - POWER_MAX: ${aggregateData[PowerRecord.POWER_MAX]}")
    }

    suspend fun readRespiratoryRateSessionData() {
        val respiratoryRateRecord = healthConnectManager.readRespiratoryRateRecord(Instant.now().minusSeconds(60 * 60 * 24), Instant.now())
        respiratoryRateRecord.forEach {
            Log.e("OnboardingViewModel", "Respiratory Rate Session Data - Respiratory Rate Session rate: ${it.rate}")
        }
    }

    suspend fun readRestingHeartRateSessionData() {
        val restingHeartRateRecord = healthConnectManager.readRestingHeartRateRecord(Instant.now().minusSeconds(60 * 60 * 24), Instant.now())
        val uid = restingHeartRateRecord.first().metadata.id
        val restingHeartRateSession = healthConnectClient.readRecord(RestingHeartRateRecord::class, uid)
        val timeRangeFilter = TimeRangeFilter.between(Instant.now().minusSeconds(60 * 60 * 24), Instant.now())
        val aggregateDataTypes = setOf(
            RestingHeartRateRecord.BPM_AVG,
            RestingHeartRateRecord.BPM_MIN,
            RestingHeartRateRecord.BPM_MAX,
        )
        val dataOriginFilter = setOf(restingHeartRateSession.record.metadata.dataOrigin)
        val aggregateRequest = AggregateRequest(
            metrics = aggregateDataTypes,
            timeRangeFilter = timeRangeFilter,
            dataOriginFilter = dataOriginFilter
        )
        val aggregateData = healthConnectClient.aggregate(aggregateRequest)
        Log.e("OnboardingViewModel", "Resting Heart Rate Session Data - BPM_AVG: ${aggregateData[RestingHeartRateRecord.BPM_AVG]}")
        Log.e("OnboardingViewModel", "Resting Heart Rate Session Data - BPM_MIN: ${aggregateData[RestingHeartRateRecord.BPM_MIN]}")
        Log.e("OnboardingViewModel", "Resting Heart Rate Session Data - BPM_MAX: ${aggregateData[RestingHeartRateRecord.BPM_MAX]}")
    }

    suspend fun readSleepSessionData() {
        val sleepRecord = healthConnectManager.readSleepSessionRecord(Instant.now().minusSeconds(60 * 60 * 24), Instant.now())
        val uid = sleepRecord.first().metadata.id
        val sleepSession = healthConnectClient.readRecord(SleepSessionRecord::class, uid)
        val timeRangeFilter = TimeRangeFilter.between(Instant.now().minusSeconds(60 * 60 * 24), Instant.now())
        val aggregateDataTypes = setOf(
            SleepSessionRecord.SLEEP_DURATION_TOTAL
        )
        val dataOriginFilter = setOf(sleepSession.record.metadata.dataOrigin)
        val aggregateRequest = AggregateRequest(
            metrics = aggregateDataTypes,
            timeRangeFilter = timeRangeFilter,
            dataOriginFilter = dataOriginFilter
        )
        val aggregateData = healthConnectClient.aggregate(aggregateRequest)
        Log.e("OnboardingViewModel", "Sleep Session Data - SLEEP_DURATION_TOTAL: ${aggregateData[SleepSessionRecord.SLEEP_DURATION_TOTAL]}")
    }

    suspend fun readSpeedSessionData() {
        val speedRecord = healthConnectManager.readSpeedRecord(Instant.now().minusSeconds(60 * 60 * 24), Instant.now())
        val uid = speedRecord.first().metadata.id
        val speedSession = healthConnectClient.readRecord(SpeedRecord::class, uid)
        val timeRangeFilter = TimeRangeFilter.between(Instant.now().minusSeconds(60 * 60 * 24), Instant.now())
        val aggregateDataTypes = setOf(
            SpeedRecord.SPEED_AVG,
            SpeedRecord.SPEED_MIN,
            SpeedRecord.SPEED_MAX,
        )
        val dataOriginFilter = setOf(speedSession.record.metadata.dataOrigin)
        val aggregateRequest = AggregateRequest(
            metrics = aggregateDataTypes,
            timeRangeFilter = timeRangeFilter,
            dataOriginFilter = dataOriginFilter
        )
        val aggregateData = healthConnectClient.aggregate(aggregateRequest)
        Log.e("OnboardingViewModel", "Speed Session Data - SPEED_AVG: ${aggregateData[SpeedRecord.SPEED_AVG]}")
        Log.e("OnboardingViewModel", "Speed Session Data - SPEED_MIN: ${aggregateData[SpeedRecord.SPEED_MIN]}")
        Log.e("OnboardingViewModel", "Speed Session Data - SPEED_MAX: ${aggregateData[SpeedRecord.SPEED_MAX]}")
    }

    suspend fun readStepsCadenceSessionData() {
        val stepsRecord = healthConnectManager.readStepsCadenceRecord(Instant.now().minusSeconds(60 * 60 * 24), Instant.now())
        val uid = stepsRecord.first().metadata.id
        val stepsSession = healthConnectClient.readRecord(StepsCadenceRecord::class, uid)
        val timeRangeFilter = TimeRangeFilter.between(Instant.now().minusSeconds(60 * 60 * 24), Instant.now())
        val aggregateDataTypes = setOf(
            StepsCadenceRecord.RATE_AVG,
            StepsCadenceRecord.RATE_MIN,
            StepsCadenceRecord.RATE_MAX,
        )
        val dataOriginFilter = setOf(stepsSession.record.metadata.dataOrigin)
        val aggregateRequest = AggregateRequest(
            metrics = aggregateDataTypes,
            timeRangeFilter = timeRangeFilter,
            dataOriginFilter = dataOriginFilter
        )
        val aggregateData = healthConnectClient.aggregate(aggregateRequest)
        Log.e("OnboardingViewModel", "Steps Cadence Session Data - RATE_AVG: ${aggregateData[StepsCadenceRecord.RATE_AVG]}")
        Log.e("OnboardingViewModel", "Steps Cadence Session Data - RATE_MIN: ${aggregateData[StepsCadenceRecord.RATE_MIN]}")
        Log.e("OnboardingViewModel", "Steps Cadence Session Data - RATE_MAX: ${aggregateData[StepsCadenceRecord.RATE_MAX]}")
    }

    suspend fun readStepsSessionData() {
        val stepsRecord = healthConnectManager.readStepsRecord(Instant.now().minusSeconds(60 * 60 * 24), Instant.now())
        val uid = stepsRecord.first().metadata.id
        val stepsSession = healthConnectClient.readRecord(StepsRecord::class, uid)
        val timeRangeFilter = TimeRangeFilter.between(Instant.now().minusSeconds(60 * 60 * 24), Instant.now())
        val aggregateDataTypes = setOf(
            StepsRecord.COUNT_TOTAL
        )
        val dataOriginFilter = setOf(stepsSession.record.metadata.dataOrigin)
        val aggregateRequest = AggregateRequest(
            metrics = aggregateDataTypes,
            timeRangeFilter = timeRangeFilter,
            dataOriginFilter = dataOriginFilter
        )
        val aggregateData = healthConnectClient.aggregate(aggregateRequest)
        Log.e("OnboardingViewModel", "Steps Session Data - COUNT_TOTAL: ${aggregateData[StepsRecord.COUNT_TOTAL]}")
    }

    suspend fun readWeightSessionData() {
        val weightRecord = healthConnectManager.readWeightRecord(Instant.now().minusSeconds(60 * 60 * 24), Instant.now())
        val uid = weightRecord.first().metadata.id
        val weightSession = healthConnectClient.readRecord(WeightRecord::class, uid)
        val timeRangeFilter = TimeRangeFilter.between(Instant.now().minusSeconds(60 * 60 * 24), Instant.now())
        val aggregateDataTypes = setOf(
            WeightRecord.WEIGHT_AVG,
            WeightRecord.WEIGHT_MIN,
            WeightRecord.WEIGHT_MAX,
        )
        val dataOriginFilter = setOf(weightSession.record.metadata.dataOrigin)
        val aggregateRequest = AggregateRequest(
            metrics = aggregateDataTypes,
            timeRangeFilter = timeRangeFilter,
            dataOriginFilter = dataOriginFilter
        )
        val aggregateData = healthConnectClient.aggregate(aggregateRequest)
        Log.e("OnboardingViewModel", "Weight Session Data - WEIGHT_AVG: ${aggregateData[WeightRecord.WEIGHT_AVG]}")
        Log.e("OnboardingViewModel", "Weight Session Data - WEIGHT_MIN: ${aggregateData[WeightRecord.WEIGHT_MIN]}")
        Log.e("OnboardingViewModel", "Weight Session Data - WEIGHT_MAX: ${aggregateData[WeightRecord.WEIGHT_MAX]}")
    }
}