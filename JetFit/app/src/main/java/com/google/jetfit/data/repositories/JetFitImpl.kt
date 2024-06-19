package com.google.jetfit.data.repositories

import com.google.jetfit.data.entities.Category
import com.google.jetfit.data.entities.Challenge
import com.google.jetfit.data.entities.FavList
import com.google.jetfit.data.entities.Intensity
import com.google.jetfit.data.entities.Language
import com.google.jetfit.data.entities.Profile
import com.google.jetfit.data.entities.Routine
import com.google.jetfit.data.entities.Series
import com.google.jetfit.data.entities.Session
import com.google.jetfit.data.entities.Song
import com.google.jetfit.data.entities.Subscription
import com.google.jetfit.data.entities.SubtitleLanguage
import com.google.jetfit.data.entities.Training
import com.google.jetfit.data.entities.TrainingDetails
import com.google.jetfit.data.entities.Workout
import com.google.jetfit.data.entities.WorkoutType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Date
import javax.inject.Inject

class JetFitImpl @Inject constructor() : JetFitRepository {

    private val dummyWorkoutData: DummyWorkoutData = DummyWorkoutData()

    override suspend fun getInstructors(): List<String> {
        return getSessions().map { it.instructor }
    }

    override fun getWorkouts(): List<Workout> = listOf(
        Workout(
            id = "1",
            name = "Strengthen & lengthen",
            description = "This workout, titled 'Strengthen & lengthen', is designed to help you increase your muscle strength and flexibility.",
            instructorName = "David Lee",
            workoutType = WorkoutType.STRENGTH,
            imageUrl = "https://github.com/TheChance101/tv-samples/assets/45900975/8e91aafe-52be-41fd-8869-977ca0410ab5",
            duration = "16",
            videoUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            intensity = Intensity.MEDIUM,
            releasedDate = Date(),
            language = Language.ENGLISH,
            subtitleLanguage = SubtitleLanguage.ENGLISH,
            subtitleUri = null
        ),
        Workout(
            id = "2",
            name = "Battle ropes HIIT",
            description = "This high-intensity interval training session with battle ropes will push your limits and improve your cardiovascular fitness.",
            instructorName = "David Lee",
            workoutType = WorkoutType.CARDIO,
            imageUrl = "https://github.com/TheChance101/tv-samples/assets/45900975/41277637-6cc9-4bcd-9a1f-b4b12d501dc8",
            duration = "14",
            videoUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            intensity = Intensity.HARD,
            releasedDate = Date(),
            language = Language.ENGLISH,
            subtitleLanguage = SubtitleLanguage.ENGLISH,
            subtitleUri = null
        ),
        Workout(
            id = "3",
            name = "Improve strength for run",
            description = "This workout, titled 'Improve strength for run', is designed to enhance your muscle strength and endurance specifically for running. It focuses on exercises that build the key muscle groups used in running, helping you run faster and longer while reducing the risk of injury.",
            instructorName = "David Lee",
            workoutType = WorkoutType.STRENGTH,
            imageUrl = "https://github.com/TheChance101/tv-samples/assets/45900975/b1773e6c-4a50-4e45-a8f2-5ff176e8167a",
            duration = "13",
            videoUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            intensity = Intensity.MEDIUM,
            releasedDate = Date(),
            language = Language.ENGLISH,
            subtitleLanguage = SubtitleLanguage.ENGLISH,
            subtitleUri = null
        ),
        Workout(
            id = "4",
            name = "Intense full body workout",
            description = "This intense full body workout will challenge your entire body and boost your strength and stamina.",
            instructorName = "David Lee",
            workoutType = WorkoutType.CARDIO,
            imageUrl = "https://github.com/TheChance101/tv-samples/assets/45900975/7927e3f0-bb09-4309-a11c-0748cd39b535",
            duration = "25",
            videoUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            intensity = Intensity.HARD,
            releasedDate = Date(),
            language = Language.ENGLISH,
            subtitleLanguage = SubtitleLanguage.ENGLISH,
            subtitleUri = null
        ),
        Workout(
            id = "5",
            name = "Workout with weights",
            description = "This workout, titled 'Workout with weights', is perfect for building muscle strength and endurance using various weight exercises.",
            instructorName = "David Lee",
            workoutType = WorkoutType.STRENGTH,
            imageUrl = "https://github.com/TheChance101/tv-samples/assets/45900975/194152ea-d535-4504-bcae-17007d47e20a",
            duration = "12",
            videoUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            intensity = Intensity.MEDIUM,
            releasedDate = Date(),
            language = Language.ENGLISH,
            subtitleLanguage = SubtitleLanguage.ENGLISH,
            subtitleUri = null
        ),
        Workout(
            id = "6",
            name = "Glutes and leg strength",
            description = "Focus on your glutes and legs with this strength workout, designed to tone and build muscle in your lower body.",
            instructorName = "David Lee",
            workoutType = WorkoutType.STRENGTH,
            imageUrl = "https://github.com/TheChance101/tv-samples/assets/45900975/f086b517-9c6a-44fa-9924-a6a5c9bde386",
            duration = "15",
            videoUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            intensity = Intensity.MEDIUM,
            releasedDate = Date(),
            language = Language.ENGLISH,
            subtitleLanguage = SubtitleLanguage.ENGLISH,
            subtitleUri = null
        ),
        Workout(
            id = "7",
            name = "Full body strength",
            description = "This full body strength workout will engage all major muscle groups to enhance your overall fitness.",
            instructorName = "David Lee",
            workoutType = WorkoutType.STRENGTH,
            imageUrl = "https://github.com/TheChance101/tv-samples/assets/45900975/e83a8d1e-5d78-4ad2-8a42-341251d77e79",
            duration = "28",
            videoUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            intensity = Intensity.HARD,
            releasedDate = Date(),
            language = Language.ENGLISH,
            subtitleLanguage = SubtitleLanguage.ENGLISH,
            subtitleUri = null
        ),
        Workout(
            id = "8",
            name = "Strength for beginners",
            description = "This beginner-friendly strength workout is designed to introduce you to the basics of strength training.",
            instructorName = "David Lee",
            workoutType = WorkoutType.STRENGTH,
            imageUrl = "https://github.com/TheChance101/tv-samples/assets/45900975/7236d183-0be8-4b4a-b6c9-e628e67c9bc3",
            duration = "10",
            videoUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            intensity = Intensity.EASY,
            releasedDate = Date(),
            language = Language.ENGLISH,
            subtitleLanguage = SubtitleLanguage.ENGLISH,
            subtitleUri = null
        ),
        Workout(
            id = "9",
            name = "Interval 5 day split",
            description = "This interval training workout is part of a 5-day split program to maximize your strength and endurance.",
            instructorName = "David Lee",
            workoutType = WorkoutType.CARDIO,
            imageUrl = "https://github.com/TheChance101/tv-samples/assets/45900975/2f0c3c1c-d40d-46c8-995d-3a524b521a8d",
            duration = "16",
            videoUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            intensity = Intensity.MEDIUM,
            releasedDate = Date(),
            language = Language.ENGLISH,
            subtitleLanguage = SubtitleLanguage.ENGLISH,
            subtitleUri = null
        ),
        Workout(
            id = "10",
            name = "Power workout",
            description = "This power-packed workout is designed to build strength, agility, and explosive power.",
            instructorName = "David Lee",
            workoutType = WorkoutType.STRENGTH,
            imageUrl = "https://github.com/TheChance101/tv-samples/assets/45900975/dd02be7b-b97b-42eb-b152-11a8579d3821",
            duration = "25",
            videoUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            intensity = Intensity.HARD,
            releasedDate = Date(),
            language = Language.ENGLISH,
            subtitleLanguage = SubtitleLanguage.ENGLISH,
            subtitleUri = null
        ),
        Workout(
            id = "11",
            name = "Beginner abs training",
            description = "This beginner abs training workout focuses on building core strength and stability.",
            instructorName = "Emily Johnson",
            workoutType = WorkoutType.STRENGTH,
            imageUrl = "https://github.com/TheChance101/tv-samples/assets/45900975/b1d53df4-2c05-4cb9-813f-0f42bcafa0d1",
            duration = "13",
            videoUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            intensity = Intensity.EASY,
            releasedDate = Date(),
            language = Language.ENGLISH,
            subtitleLanguage = SubtitleLanguage.ENGLISH,
            subtitleUri = null
        ),
        Workout(
            id = "12",
            name = "Working in the gym",
            description = "This gym-focused workout emphasizes traditional exercises to help you build muscle and improve overall fitness.",
            instructorName = "Emily Johnson",
            workoutType = WorkoutType.STRENGTH,
            imageUrl = "https://github.com/TheChance101/tv-samples/assets/45900975/90c1f376-adca-4915-9fdf-4c871008a75b",
            duration = "20",
            videoUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            intensity = Intensity.MEDIUM,
            releasedDate = Date(),
            language = Language.ENGLISH,
            subtitleLanguage = SubtitleLanguage.ENGLISH,
            subtitleUri = null
        )
    )


    override fun getWorkoutById(id: String): Workout = getWorkouts().first { it.id == id }

    override fun getSeries(): List<Series> = listOf(
        Series(
            "1",
            "Interval 5 day split",
            "Maximize your workouts with split training! Increase strength and build muscle in this 1-week guided program using heavier weights and targeting specific areas of the body in each workout. Repeat and track your progress!",
            "Andrew Trace",
            WorkoutType.SESSIONS,
            "https://github.com/TheChance101/tv-samples/assets/45900975/796ed7dc-9e22-465b-b7c4-0ed9371f40c0",
            1L,
            5,
            30,
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            Intensity.HARD,
            Date(),
            Language.ENGLISH,
            SubtitleLanguage.ENGLISH
        ),
        Series(
            "2",
            "30-Day Cardio Challenge",
            "Take your fitness to the next level with our 30-day cardio challenge. Join trainer Alex Rodriguez in a series of high-intensity workouts designed to boost your endurance and burn calories.",
            "Alex Rodriguez",
            WorkoutType.CARDIO,
            "https://github.com/TheChance101/tv-samples/assets/45900975/e46ebf4f-75f1-4fe9-81ad-a9a4c5775002",
            4L,
            20,
            45,
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            Intensity.HARD,
            Date(),
            Language.ENGLISH,
            SubtitleLanguage.ENGLISH
        ),

        Series(
            "3",
            "Strength Training Bootcamp",
            "Transform your physique with our intense strength training bootcamp. Led by fitness expert Mike Smith, this series includes challenging workouts to build muscle and increase strength.",
            "Mike Smith",
            WorkoutType.STRENGTH,
            "https://github.com/TheChance101/tv-samples/assets/45900975/a47e205f-29ce-4bfc-82b1-323cd229cf98",
            6L,
            24,
            40,
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            Intensity.MEDIUM,
            Date(),
            Language.ENGLISH,
            SubtitleLanguage.ENGLISH
        ),
        Series(
            "4",
            "Beginner Pilates Program",
            "Discover the benefits of Pilates with this beginner-friendly program. Led by instructor Emily White, this series focuses on core strength, flexibility, and body awareness.",
            "Emily White",
            WorkoutType.STRENGTH,
            "https://github.com/TheChance101/tv-samples/assets/45900975/104cf1fb-d82c-48f0-a188-9b31389165e8",
            4L,
            16,
            35,
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            Intensity.EASY,
            Date(),
            Language.ENGLISH,
            SubtitleLanguage.ENGLISH
        ),
        Series(
            "5",
            "Yoga for Stress Relief",
            "Relieve stress and unwind with our yoga series designed to promote relaxation and mental well-being. Join instructor Lisa Taylor in a series of gentle yoga practices and guided meditation sessions.",
            "Lisa Taylor",
            WorkoutType.YOGA,
            "https://github.com/TheChance101/tv-samples/assets/45900975/cebdd805-953f-4ef1-965c-08741fc5c976",
            3L,
            12,
            25,
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            Intensity.EASY,
            Date(),
            Language.ENGLISH,
            SubtitleLanguage.ENGLISH
        ),
        Series(
            "6",
            "Total Body Burn",
            "Torch calories and sculpt your physique with this high-intensity total body workout series. Led by trainer Chris Johnson, each session targets multiple muscle groups for maximum results.",
            "Chris Johnson",
            WorkoutType.STRENGTH,
            "https://github.com/TheChance101/tv-samples/assets/45900975/be2f2a01-d63b-4e75-8729-7a09984ca371",
            5L,
            20,
            40,
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            Intensity.HARD,
            Date(),
            Language.ENGLISH,
            SubtitleLanguage.ENGLISH
        ),
        Series(
            "7",
            "Prenatal Yoga Series",
            "Stay active and prepare for childbirth with our prenatal yoga series. Led by experienced instructor Rachel Adams, these gentle practices focus on strengthening the body and calming the mind during pregnancy.",
            "Rachel Adams",
            WorkoutType.YOGA,
            "https://github.com/TheChance101/tv-samples/assets/45900975/7f73990b-99f8-459e-adbc-5fdbb5e5c512",
            3L,
            10,
            30,
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            Intensity.EASY,
            Date(),
            Language.ENGLISH,
            SubtitleLanguage.ENGLISH
        ),
        Series(
            "8",
            "Advanced HIIT Workouts",
            "Challenge yourself with our advanced HIIT series designed for experienced fitness enthusiasts. Led by trainer Mark Thompson, these high-intensity interval workouts will push your limits and take your fitness to new heights.",
            "Mark Thompson",
            WorkoutType.CARDIO,
            "https://github.com/TheChance101/tv-samples/assets/45900975/3aadb620-5ae9-4eba-8be7-a49b59ce0204",
            6L,
            18,
            50,
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            Intensity.HARD,
            Date(),
            Language.ENGLISH,
            SubtitleLanguage.ENGLISH
        ),
        Series(
            "9",
            "Core Strength Challenge",
            "Build a strong and stable core with our intensive core strength challenge series. Led by instructor Jessica Miller, these targeted workouts will help you develop abdominal strength and improve posture.",
            "Jessica Miller",
            WorkoutType.STRENGTH,
            "https://github.com/TheChance101/tv-samples/assets/45900975/23f28376-d089-4a34-bcb5-38c8e32cbc11",
            4L,
            16,
            35,
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            Intensity.MEDIUM,
            Date(),
            Language.ENGLISH,
            SubtitleLanguage.ENGLISH
        )
    )

    override fun getSeriesById(id: String): Series = getSeries().first { it.id == id }

    override fun getRoutines(): List<Routine> = listOf(
        Routine(
            "1",
            "10 Morning Exercises",
            "Don’t let mornings put you in a bad mood! Make your day so much better by launching yourself off your bed and getting in to a full-on workout mode.",
            "Rachel Wright",
            WorkoutType.YOGA,
            "https://github.com/TheChance101/tv-samples/assets/45900975/c8b28144-06b2-42c6-b87f-464e78d3e706",
            10L,
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            Intensity.MEDIUM,
            Date(),
            Language.ENGLISH,
            SubtitleLanguage.ENGLISH
        ),
        Routine(
            "2",
            "Morning Stretch Routine",
            "Start your day with this gentle stretching routine designed to awaken your body and improve flexibility. Led by instructor Sarah Johnson, this routine will leave you feeling refreshed and ready for the day ahead.",
            "Sarah Johnson",
            WorkoutType.STRENGTH,
            "https://static.nike.com/a/images/f_auto/dpr_3.0,cs_srgb/w_403,c_limit/d081550e-586a-4567-9a82-6c96243d27b1/how-to-create-a-morning-stretch-routine-at-home.jpg",
            20L,
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            Intensity.EASY,
            Date(),
            Language.ENGLISH,
            SubtitleLanguage.ENGLISH
        ),
        Routine(
            "3",
            "Full Body Circuit Training",
            "Get a complete workout in just 30 minutes with this full-body circuit training routine. Led by trainer Alex Rodriguez, this high-intensity workout combines strength exercises with cardio intervals to maximize results.",
            "Alex Rodriguez",
            WorkoutType.STRENGTH,
            "https://i0.wp.com/www.muscleandfitness.com/wp-content/uploads/2019/07/Male-Bodybuilder-Standing-Bench-Barbell.jpg?quality=86&strip=all",
            30L,
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            Intensity.MEDIUM,
            Date(),
            Language.ENGLISH,
            SubtitleLanguage.ENGLISH
        ),
        Routine(
            "4",
            "Evening Yoga Relaxation",
            "Wind down and release tension with this evening yoga relaxation routine. Led by instructor Emily White, this gentle practice focuses on deep breathing and restorative poses to promote relaxation and stress relief.",
            "Emily White",
            WorkoutType.YOGA,
            "https://surfershype.com/wp-content/uploads/2021/03/surfershealth.jpg",
            25L,
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            Intensity.EASY,
            Date(),
            Language.ENGLISH,
            SubtitleLanguage.ENGLISH
        ),
        Routine(
            "5",
            "Core Strengthening Workout",
            "Develop a strong and stable core with this core strengthening workout routine. Led by instructor Mike Smith, this series of exercises targets your abdominal muscles to improve strength and posture.",
            "Mike Smith",
            WorkoutType.STRENGTH,
            "https://blog.fitbit.com/wp-content/uploads/2019/03/0305_QuickWorkouts_BLOG-730x485.jpg",
            40L,
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            Intensity.MEDIUM,
            Date(),
            Language.ENGLISH,
            SubtitleLanguage.ENGLISH
        ),
        Routine(
            "6",
            "Cardio Blast",
            "Boost your cardiovascular fitness with this intense cardio blast routine. Led by instructor Jessica Miller, this high-energy workout includes dynamic movements to elevate your heart rate and burn calories.",
            "Jessica Miller",
            WorkoutType.CARDIO,
            "https://www.afcfitness.com/wp-content/uploads/2018/11/cardio-blast.jpg",
            35L,
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            Intensity.HARD,
            Date(),
            Language.ENGLISH,
            SubtitleLanguage.ENGLISH
        ),
        Routine(
            "7",
            "Quick Abs Burn",
            "Get ready to feel the burn with this quick abs workout routine. Led by instructor David Lee, this intense session targets your abdominal muscles to help sculpt and define your midsection.",
            "David Lee",
            WorkoutType.STRENGTH,
            "https://www.eatthis.com/wp-content/uploads/sites/4/2024/02/muscular-man-medicine-ball-slam.jpeg?quality=82&strip=1",
            15L,
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            Intensity.HARD,
            Date(),
            Language.ENGLISH,
            SubtitleLanguage.ENGLISH
        ),
        Routine(
            "8",
            "Total Body Stretch",
            "Release tension and improve flexibility with this total body stretch routine. Led by instructor Maria Garcia, this gentle sequence of stretches targets all major muscle groups to help you feel relaxed and rejuvenated.",
            "Maria Garcia",
            WorkoutType.STRENGTH,
            "https://res.cloudinary.com/peloton-cycle/image/fetch/dpr_1.0,f_auto,q_auto:good,w_1800/https://s3.amazonaws.com/peloton-ride-images/50abeae651ede2a69fcea4946abaca291f34176d/img_1690575454_940b757cd2d8475a9151ad5b219e381e.png",
            25L,
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            Intensity.EASY,
            Date(),
            Language.ENGLISH,
            SubtitleLanguage.ENGLISH
        ),
        Routine(
            "9",
            "Power Yoga Flow",
            "Ignite your inner strength with this power yoga flow routine. Led by instructor Rachel Adams, this dynamic sequence of poses focuses on building strength, flexibility, and balance.",
            "Rachel Adams",
            WorkoutType.YOGA,
            "https://vesselpilates.com/wp-content/uploads/sites/5225/2022/07/DSC9154-1024x684.jpg",
            40L,
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            Intensity.MEDIUM,
            Date(),
            Language.ENGLISH,
            SubtitleLanguage.ENGLISH
        ),
        Routine(
            "10",
            "Interval Training Challenge",
            "Push your limits with this interval training challenge routine. Led by trainer Chris Johnson, this high-intensity workout alternates between bursts of intense exercise and brief recovery periods to maximize calorie burn and improve fitness.",
            "Chris Johnson",
            WorkoutType.CARDIO,
            "https://image.boxrox.com/2022/10/Elderly-workout-822x466.jpg",
            50L,
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            Intensity.HARD,
            Date(),
            Language.ENGLISH,
            SubtitleLanguage.ENGLISH
        )
    )

    override fun getRoutineById(id: String): Routine = getRoutines().first { it.id == id }

    override fun getChallenges(): List<Challenge> =
        listOf(
            Challenge(
                "1",
                "30 Days of HIIT & mindfulness",
                "Build your full body endurance with high-intensity training drills, kick boxing and more. Quick workouts to warm up before or cool down after your run.",
                "Hugo Wright",
                WorkoutType.CHALLENGE,
                "https://github.com/TheChance101/tv-samples/assets/45900975/9b03ce65-ffbd-40d9-876b-9764f3121905",
                30,
                15,
                generateWeeklyPlans(getWorkouts()),
                Intensity.EASY,
                Date(),
                Language.ENGLISH,
                SubtitleLanguage.ENGLISH
            ),
            Challenge(
                "2",
                "30-Day Cardio Challenge",
                "Join our 30-day cardio challenge and boost your fitness level with high-intensity cardio workouts. Led by expert trainer Alex Rodriguez, this challenge will push your limits and help you achieve your fitness goals.",
                "Alex Rodriguez",
                WorkoutType.CARDIO,
                "https://github.com/TheChance101/tv-samples/assets/45900975/87419c7f-3a7e-4cba-bc50-75c6a4365103",
                45,
                30,
                generateWeeklyPlans(getWorkouts()),
                Intensity.HARD,
                Date(),
                Language.ENGLISH,
                SubtitleLanguage.ENGLISH
            ),
            Challenge(
                "3",
                "21-Day Yoga Journey",
                "Embark on a transformative 21-day yoga journey to cultivate inner peace and physical strength. Led by renowned instructor Sarah Johnson, this challenge includes daily yoga practices designed to rejuvenate your mind, body, and soul.",
                "Sarah Johnson",
                WorkoutType.YOGA,
                "https://github.com/TheChance101/tv-samples/assets/45900975/902b57c7-9417-4ba3-a63a-2fbe1326e2f9",
                30,
                21,
                generateWeeklyPlans(getWorkouts()),
                Intensity.MEDIUM,
                Date(),
                Language.ENGLISH,
                SubtitleLanguage.ENGLISH
            ),
            Challenge(
                "4",
                "14-Day Strength Training Challenge",
                "Build muscle and increase strength with our 14-day strength training challenge. Led by fitness expert Mike Smith, this challenge includes daily workouts targeting different muscle groups to help you sculpt your ideal physique.",
                "Mike Smith",
                WorkoutType.STRENGTH,
                "https://github.com/TheChance101/tv-samples/assets/45900975/c6a47503-0946-45d5-891a-7b1b3578d0f9",
                40,
                14,
                generateWeeklyPlans(getWorkouts()),
                Intensity.MEDIUM,
                Date(),
                Language.ENGLISH,
                SubtitleLanguage.ENGLISH
            )
        )

    override fun getChallengeById(id: String): Challenge = getChallenges().first { it.id == id }

    private fun generateWeeklyPlans(availableWorkouts: List<Workout>): List<Pair<String, List<Workout>>> {
        val shuffledWorkouts = availableWorkouts.shuffled().take(5)
        return listOf(
            "Week 1: Meet the basics" to shuffledWorkouts.shuffled(),
            "Week 2: Get your footing" to shuffledWorkouts.shuffled(),
            "Week 3: Expand your style" to shuffledWorkouts.shuffled(),
            "Week 4: Challenge yourself" to shuffledWorkouts.shuffled()
        )
    }

    override fun getFavoritesWorkouts() = flow {
        emit(FavList(value = dummyWorkoutData.list))
    }

    override fun getSongById(id: String): Song {
        return Song(
            id = "123456sdasdsa",
            title = "Power Workout",
            author = "Jake Diaz",
            createdDate = "2021",
            audioUrl = "",
            imageUrl = "https://images.unsplash.com/photo-1604480133080-602261a680df?q=80&w=1770&auto=format&fit=crop&ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
        )
    }

    override suspend fun getSessions(): List<Session> {
        return listOf(
            Session(
                id = "1",
                instructor = "Danielle Orlando",
                title = "Strengthen & lengthen pilates",
                description = "This pilates workout is perfect for good balance between your overall strength and flexibility. Use your own body weight to strengthen and sculpt our muscles",
                imageUrl = "https://cdn.muscleandstrength.com/sites/default/files/strong-brunette-doing-shoulder-press.jpg"
            ),
            Session(
                id = "2",
                instructor = "John Smith",
                title = "Yoga Flow",
                description = "Join us for a relaxing yoga flow session to unwind and improve flexibility. Suitable for all levels.",
                imageUrl = "https://1.bp.blogspot.com/-06DVesUYzOQ/Xxff6Ysq8VI/AAAAAAAABJ0/HljMYwQN9iEOcuBIRrTmzVMiYQWekkvWgCLcBGAsYHQ/s640/vinyasa.jpg"
            ),
            Session(
                id = "3",
                instructor = "Emily Johnson",
                title = "HIIT Workout",
                description = "Get ready to sweat with our high-intensity interval training (HIIT) session. Burn calories, build strength, and improve endurance!",
                imageUrl = "https://static01.nyt.com/images/2023/03/21/multimedia/14WNT-HIIT-WORKOUTS1-lktg/14WNT-HIIT-WORKOUTS1-lktg-videoSixteenByNine3000.jpg"
            ),
            Session(
                id = "4",
                instructor = "Michael Thompson",
                title = "Boxing Bootcamp",
                description = "Punch and kick your way to fitness with our boxing bootcamp! Improve your coordination, agility, and strength while having fun.",
                imageUrl = "https://clubwoodside.com/wp-content/uploads/2019/02/Woodside_GX_Boxing-1080x675-1.jpg"
            ),
        )

    }

    override suspend fun getCategories(): List<Category> {
        return listOf(
            Category(
                id = "1",
                imageUrl = "https://github.com/TheChance101/tv-samples/assets/45900975/2cf237b8-8a2e-4887-a037-6c6b45c5e46d",
                title = "Yoga & Pilates",
                description = "There are many benefits to yoga and Pilates, including increased..."
            ),
            Category(
                id = "2",
                imageUrl = "https://github.com/TheChance101/tv-samples/assets/45900975/d40c6512-0f49-4912-892f-33768f66733f",
                title = "Strength training",
                description = "Strength training makes you stronger, helps you control your..."
            ),
            Category(
                id = "3",
                imageUrl = "https://github.com/TheChance101/tv-samples/assets/45900975/ca924680-2e28-49db-95ff-bc1a9e04a079",
                title = "Aerobic & Cardio",
                description = "Get your blood pumping and build up your endurance with some..."
            ),
        )
    }

    override suspend fun getTrainingsRecommended(): List<Training> {
        return listOf(
            Training(
                id = "1",
                instructor = "Intensity",
                title = "Full body strength",
                time = "26 Min",
                imageUrl = "https://github.com/TheChance101/tv-samples/assets/45900975/e3d5daad-5620-4486-b231-a220e23f5bd9"
            ),
            Training(
                id = "2",
                instructor = "Intensity",
                title = "Total-body balance pilates",
                time = "24 Min",
                imageUrl = "https://github.com/TheChance101/tv-samples/assets/45900975/4f8b6fd6-55a3-4dfa-a642-a4370b66e253"
            ),
            Training(
                id = "3",
                instructor = "Intensity",
                title = "Circuit training",
                time = "13 Min",
                imageUrl = "https://github.com/TheChance101/tv-samples/assets/45900975/c7d8dadc-258e-4c09-a8b6-3d1d68c42d75"
            ),
            Training(
                id = "4",
                instructor = "Intensity",
                title = "Morning stretch",
                time = "15 Min",
                imageUrl = "https://github.com/TheChance101/tv-samples/assets/45900975/43d2477f-c498-465e-8923-23fc9226d83b"
            ),
            Training(
                id = "5",
                instructor = "Intensity",
                title = "Yoga",
                time = "20 Min",
                imageUrl = "https://github.com/TheChance101/tv-samples/assets/45900975/43c5fefe-1ee3-408f-8277-d5cde97e900c"
            ),
            Training(
                id = "6",
                instructor = "Intensity",
                title = "Wind down",
                time = "16 Min",
                imageUrl = "https://github.com/TheChance101/tv-samples/assets/45900975/8f59bc45-0b1e-4948-a7e9-e432fa44a4b6"
            ),
            Training(
                id = "7",
                instructor = "Intensity",
                title = "Interval 5 day split",
                time = "16 Min",
                imageUrl = "https://github.com/TheChance101/tv-samples/assets/45900975/2c2be937-c0c5-48c9-992b-d2315d3a0752"
            ),
            Training(
                id = "8",
                instructor = "Intensity",
                title = "Working in the gym",
                time = "20 Min",
                imageUrl = "https://github.com/TheChance101/tv-samples/assets/45900975/9ce1d637-fa28-4691-ae34-4a72693ab746"
            )
        )
    }

    override suspend fun getUserProfiles(): List<Profile> {
        return listOf(
            Profile(
                id = "1",
                name = "Liam",
                avatar = "https://github.com/TheChance101/tv-samples/assets/45900975/7bd092a0-6428-45d5-91dd-4305950f2d76"
            ),
            Profile(
                id = "2",
                name = "Olivia",
                avatar = "https://github.com/TheChance101/tv-samples/assets/45900975/06275484-da42-4d49-92ec-18ce0cb470e9"
            ),
            Profile(
                id = "3",
                name = "Noah",
                avatar = "https://github.com/TheChance101/tv-samples/assets/45900975/42321083-bc05-45ca-8f80-dacdc5295c44"
            ),
        )
    }

    override fun getTrainingById(id: Int): Flow<TrainingDetails> {
        return flow {
            emit(
                TrainingDetails(
                    id = "1",
                    instructor = "Danielle Orlando",
                    type = "Intensity",
                    title = "Total-body balance pilates",
                    time = "34 Min",
                    description = "Andrea's signature low-impact, total-body class in just 30 minutes. Hit every muscle group with barre and Pilates moves that leave you feeling strong, refreshed, and energized",
                    imageUrl = "https://github.com/TheChance101/tv-samples/assets/45900975/25ccc101-fb05-4195-9ea3-432bbc585b45"
                )
            )
        }
    }

    override fun getSubscriptionOptionsByInstructorId(instructorId: String): Flow<List<Subscription>> {
        return flow {
            emit(
                listOf(
                    Subscription(
                        periodTime = "1",
                        price = "$7.99",
                    ),
                    Subscription(
                        periodTime = "3",
                        price = "$19.99",
                    ),
                    Subscription(
                        periodTime = "12",
                        price = "$79.99",
                    ),
                )
            )
        }
    }

    override suspend fun getInstructorImageById(instructorId: String): String {
        return "https://github.com/TheChance101/tv-samples/assets/45900975/ad94de6b-5e1e-45b0-b62a-0927dde1aa04"
    }
}