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
            "1",
            "Back and leg strength",
            "Strong legs do more than look good. Even the simplest daily movements like walking require leg strength. But you may wonder where to begin. Let’s dive in.",
            "Charlotte Aldridge",
            WorkoutType.STRENGTH,
            "https://figmage.com/images/FbgDhnXP05yKpdsLJfTQo.png",
            "30",
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            Intensity.HARD,
            Date(),
            Language.ENGLISH,
            SubtitleLanguage.ENGLISH,
            null
        ),
        Workout(
            "2",
            "Morning Yoga Flow",
            "Start your day with an energizing yoga routine. This flow focuses on awakening the body and mind, helping you feel refreshed and ready to tackle the day ahead.",
            "Emma Smith",
            WorkoutType.YOGA,
            "https://s3-alpha-sig.figma.com/img/aad6/dfa7/e5a9a9dd732084d30328dceec998666f?Expires=1711324800&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4&Signature=Sr8NN2cvX3Bc~OmscqwR6VnQhe0Fri9~rM-D~i39wva-lyN6xG1ai7iNXYYa5Kn~uuDhyZ-0BvYVIM~Z2GciW~YMsZzPKyvfnMk92MZ3aVc1ik2fJmOG2SknBSxBuBO0lNRS0HNq-Zx~qe7JFfHC0gadAzzvCH4bB7sI01~HYYQzRjb9EYJdzkd9GkLXD9PaN0iN7in6QRDHSZdgrhT22J96TC3wxRH2mpfFsm3LYA0iSvdZgt6Zj11xrrLlfIvnGVST-SqYnpiv5ixoCHoS18N6Q0KALn7IBH3xk8MSWRnpdfwfjE-CtITEKfOc3YKOCkciHbuhasbdEYyNMHGGcg__",
            "30",
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            Intensity.EASY,
            Date(),
            Language.ENGLISH,
            SubtitleLanguage.ENGLISH,
            null
        ),
        Workout(
            "3",
            "Strengthen & lengthen",
            "Experience an intense workout designed to maximize calorie burn and boost metabolism. This high-energy session combines bursts of intense exercise with short recovery periods for maximum results.",
            "John Doe",
            WorkoutType.CARDIO,
            "https://s3-alpha-sig.figma.com/img/5b76/28da/51c6b4c0076ea7b92c70d82dc1828425?Expires=1710720000&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4&Signature=M1NOL3tptpUBJ4qM0QbqmkCQBAKjWpNw3llpK70HUmcVyUPF9StrimFkFA32ziyk-X8GQz8VJHrT42ovbtj3ROiDwLBbLfpCbkuNaThYT5D0BAVkRZtSjkp~w3yDLQKdRSWp~1pn242mMj5ASFpYjL9udDM4JBHn9gjvzST7QGzvOHes9ZABFtimxVBC0Ot-eQDpDV7mbU9Pf5ROC2JTEd2LK-QG85N0Vv8cIFpUcPJGSFgR1tbHxMDv1GpKAx33eSGnH02~ow3R6sZm88wznn0AaPJoKwGGvU2ZJUVl6wbUD4JRt9gcs3q9FVFFEhSeoOpYbJSdqgWdzhPM-Lv-7Q__",
            "30",
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            Intensity.HARD,
            Date(),
            Language.ENGLISH,
            SubtitleLanguage.ENGLISH,
            null
        ),
        Workout(
            "4",
            "Improve strength for run",
            "Discover the fundamentals of strength training with this beginner-friendly workout. Learn proper form and technique for key exercises to build a strong foundation for future progress.",
            "Maria Garcia",
            WorkoutType.STRENGTH,
            "https://s3-alpha-sig.figma.com/img/5b76/28da/51c6b4c0076ea7b92c70d82dc1828425?Expires=1710720000&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4&Signature=M1NOL3tptpUBJ4qM0QbqmkCQBAKjWpNw3llpK70HUmcVyUPF9StrimFkFA32ziyk-X8GQz8VJHrT42ovbtj3ROiDwLBbLfpCbkuNaThYT5D0BAVkRZtSjkp~w3yDLQKdRSWp~1pn242mMj5ASFpYjL9udDM4JBHn9gjvzST7QGzvOHes9ZABFtimxVBC0Ot-eQDpDV7mbU9Pf5ROC2JTEd2LK-QG85N0Vv8cIFpUcPJGSFgR1tbHxMDv1GpKAx33eSGnH02~ow3R6sZm88wznn0AaPJoKwGGvU2ZJUVl6wbUD4JRt9gcs3q9FVFFEhSeoOpYbJSdqgWdzhPM-Lv-7Q__",
            "30",
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            Intensity.MEDIUM,
            Date(),
            Language.ENGLISH,
            SubtitleLanguage.SPANISH,
            null
        ),
        Workout(
            "5",
            "Workout with weights",
            "Unwind and release tension with this gentle evening stretch routine. Focus on deep breathing and gentle stretches to soothe tired muscles and promote relaxation, preparing you for a restful night's sleep.",
            "David Lee",
            WorkoutType.YOGA,
            "https://s3-alpha-sig.figma.com/img/5b76/28da/51c6b4c0076ea7b92c70d82dc1828425?Expires=1710720000&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4&Signature=M1NOL3tptpUBJ4qM0QbqmkCQBAKjWpNw3llpK70HUmcVyUPF9StrimFkFA32ziyk-X8GQz8VJHrT42ovbtj3ROiDwLBbLfpCbkuNaThYT5D0BAVkRZtSjkp~w3yDLQKdRSWp~1pn242mMj5ASFpYjL9udDM4JBHn9gjvzST7QGzvOHes9ZABFtimxVBC0Ot-eQDpDV7mbU9Pf5ROC2JTEd2LK-QG85N0Vv8cIFpUcPJGSFgR1tbHxMDv1GpKAx33eSGnH02~ow3R6sZm88wznn0AaPJoKwGGvU2ZJUVl6wbUD4JRt9gcs3q9FVFFEhSeoOpYbJSdqgWdzhPM-Lv-7Q__",
            "30",
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            Intensity.EASY,
            Date(),
            Language.ENGLISH,
            SubtitleLanguage.ENGLISH,
            null
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
            "https://figmage.com/images/FbgDhnXP05yKpdsLJfTQo.png",
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
            "https://example.com/cardio_challenge.jpg",
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
            "https://example.com/strength_training_bootcamp.jpg",
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
            "https://example.com/beginner_pilates_program.jpg",
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
            "https://example.com/yoga_stress_relief.jpg",
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
            "https://example.com/total_body_burn.jpg",
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
            "https://example.com/prenatal_yoga.jpg",
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
            "https://example.com/advanced_hiit.jpg",
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
            "https://example.com/core_strength_challenge.jpg",
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