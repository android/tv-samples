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
            "https://example.com/cardio_challenge_video.mp4",
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
            "https://example.com/cardio_challenge_video.mp4",
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
            "https://example.com/strength_training_bootcamp_video.mp4",
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
            "https://example.com/beginner_pilates_program_video.mp4",
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
            "https://example.com/yoga_stress_relief_video.mp4",
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
            "https://example.com/total_body_burn_video.mp4",
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
            "https://example.com/prenatal_yoga_video.mp4",
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
            "https://example.com/advanced_hiit_video.mp4",
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
            "https://example.com/core_strength_challenge_video.mp4",
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
            "https://figmage.com/images/FbgDhnXP05yKpdsLJfTQo.png",
            10L,
            "https://example.com/morning_stretch_video.mp4",
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
            "https://example.com/morning_stretch.jpg",
            20L,
            "https://example.com/morning_stretch_video.mp4",
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
            "https://example.com/full_body_circuit.jpg",
            30L,
            "https://example.com/full_body_circuit_video.mp4",
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
            "https://example.com/evening_yoga_relaxation.jpg",
            25L,
            "https://example.com/evening_yoga_relaxation_video.mp4",
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
            "https://example.com/core_strengthening.jpg",
            40L,
            "https://example.com/core_strengthening_video.mp4",
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
            "https://example.com/cardio_blast.jpg",
            35L,
            "https://example.com/cardio_blast_video.mp4",
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
            "https://example.com/quick_abs_burn.jpg",
            15L,
            "https://example.com/quick_abs_burn_video.mp4",
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
            "https://example.com/total_body_stretch.jpg",
            25L,
            "https://example.com/total_body_stretch_video.mp4",
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
            "https://example.com/power_yoga_flow.jpg",
            40L,
            "https://example.com/power_yoga_flow_video.mp4",
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
            "https://example.com/interval_training_challenge.jpg",
            50L,
            "https://example.com/interval_training_challenge_video.mp4",
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
                "https://figmage.com/images/FbgDhnXP05yKpdsLJfTQo.png",
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
                "https://example.com/cardio_challenge.jpg",
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
                "https://example.com/yoga_journey.jpg",
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
                "https://example.com/strength_training_challenge.jpg",
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
                imageUrl = "https://s3-alpha-sig.figma.com/img/5b76/28da/51c6b4c0076ea7b92c70d82dc1828425?Expires=1710720000&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4&Signature=M1NOL3tptpUBJ4qM0QbqmkCQBAKjWpNw3llpK70HUmcVyUPF9StrimFkFA32ziyk-X8GQz8VJHrT42ovbtj3ROiDwLBbLfpCbkuNaThYT5D0BAVkRZtSjkp~w3yDLQKdRSWp~1pn242mMj5ASFpYjL9udDM4JBHn9gjvzST7QGzvOHes9ZABFtimxVBC0Ot-eQDpDV7mbU9Pf5ROC2JTEd2LK-QG85N0Vv8cIFpUcPJGSFgR1tbHxMDv1GpKAx33eSGnH02~ow3R6sZm88wznn0AaPJoKwGGvU2ZJUVl6wbUD4JRt9gcs3q9FVFFEhSeoOpYbJSdqgWdzhPM-Lv-7Q__",
                title = "Yoga & Pilates",
                description = "There are many benefits to yoga and Pilates, including increased..."
            ),
            Category(
                id = "2",
                imageUrl = "https://s3-alpha-sig.figma.com/img/9e9e/d62b/548d491bf82b615ac52bb7b20d5370e4?Expires=1710720000&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4&Signature=WkhebeHHgGiGxNoLU1V2VpriPOZagVf52bfaK~tadSr-C8DmUe5N~lcK8-cN~v2fwO2Y4pcekzf8EsIR72q1KQaOzYe~qcfUpb2VO8PD-vff-jJG2MQxQcOnQfZShjx4K9ttiOg2AfWYwMfMClsxrKHmk0gVDZl1owjwqKzRILlWC8zNZyLjzGijaOo0gHFRmpYMtXOcCn~IQhDkVo-X6lRx-4jb45AiMKLWLxjU3zjCFf1VLW1MbnaSNoyoPj4~4lIsXoTFcrAdQFrGhcjhArEgC7LgEMnJDJDigxipLJ3-wN5uuYNS7bz5CfsD1QzlYnjnp-ZsNbmT9kuEnVx-Dg__",
                title = "Strength training",
                description = "Strength training makes you stronger, helps you control your..."
            ),
            Category(
                id = "3",
                imageUrl = "https://s3-alpha-sig.figma.com/img/c823/37df/f97a3d198ca7a463a530cdf43275e329?Expires=1710720000&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4&Signature=k9CLo2xpsEn07zCJElLridjmm4QdPO~oGTfQmSV1upDJZUteGoeHO~nvz8Jx7ZfZUiSyfg2rxMAWQPPgHncj9I-gHWgy9UTYWbiWuYq7ynct-4jdFdgPr1ltdoFsBRJR1~I05Js~YQi8rOUTdZy--9saib9eF~zGz10kC274EI2VT3V3xsF7XWErz2IDQ5LyDaO5rC1cS4S5H3tvzu20QlW8Kh73VaMMW70HydqB-rPrBEwljC9l3nIx8PK7BUHXxoNj0xPHOizXd6DN-JlHspxoU6N40wz4OfZc9oar-ZLGbT3mwHZrRdM0pUoHoIMr1flai50tU1t6jl-qw2QVPw__",
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
                imageUrl = "https://s3-alpha-sig.figma.com/img/c823/37df/f97a3d198ca7a463a530cdf43275e329?Expires=1710720000&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4&Signature=k9CLo2xpsEn07zCJElLridjmm4QdPO~oGTfQmSV1upDJZUteGoeHO~nvz8Jx7ZfZUiSyfg2rxMAWQPPgHncj9I-gHWgy9UTYWbiWuYq7ynct-4jdFdgPr1ltdoFsBRJR1~I05Js~YQi8rOUTdZy--9saib9eF~zGz10kC274EI2VT3V3xsF7XWErz2IDQ5LyDaO5rC1cS4S5H3tvzu20QlW8Kh73VaMMW70HydqB-rPrBEwljC9l3nIx8PK7BUHXxoNj0xPHOizXd6DN-JlHspxoU6N40wz4OfZc9oar-ZLGbT3mwHZrRdM0pUoHoIMr1flai50tU1t6jl-qw2QVPw__",
            ),
            Training(
                id = "1",
                instructor = "Intensity",
                title = "Total-body balance pilates",
                time = "24 Min",
                imageUrl = "https://s3-alpha-sig.figma.com/img/4a55/976b/4326c161fb1a8e1619b6b935a7d72898?Expires=1711324800&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4&Signature=FrisEgVcxRsPdV5~7TFJuogCRC1DGQncvd7W3eEWrE3raW3WU-NFGMg9-G3rrUAanAM8doc5Ce842G-vEyVzC~eQyY8Sl2X9RJW199oajHOcVq4QBhjWmJBbSiQiJjEm5sqGgyPSUvpWd2D-5b1d7GeSFvRPAnmR-nfnHTlmtGkc3c1y4awXIyWPvzRAxqEwJN~3lsPxAOA~4c7YM5h9tJbM7GbBru~NOdU1cP5tRF52~~H0xgebbcOU1hst5UHvDph-7zsViDPCOWvAJrAwKLF8Jzd1Ts-1BiHsVqFVROTu6eA4pj9t7u7omBGcc0XplFfJobo7YG8pFKJSwKPOrQ__",
            ),
            Training(
                id = "1",
                instructor = "Intensity",
                title = "Circuit training",
                time = "13 Min",
                imageUrl = "https://s3-alpha-sig.figma.com/img/d345/131c/29f5dc5fd473cadbe52d66e9e6a86716?Expires=1711324800&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4&Signature=M2CrOifodD92DDgx1V8n59yfLeTSMsCd6RuyEqn7YbB2bKrBqUuEUbHPmGSRuNOPMD1F1ZdvAifFhi6sQDIx430o4J1CgRao3UZ67pkdTer3xFtmHT4bLyb3H1n2BzHh9qx002v-JI-DJSVycS7AlHqCsAdyfYJNhT0DUKfugg0yK62JzjNcrm8Yy0t4qNY79xOXXq~zeprTLam4x7bk1iRCChkxK~QE1kqcfKNjOIwS0VBLPXUPobI4dVev61nJ9yOohr~RV6xEPtzWeJ7vDe0xBBMlKzhYTG75Q82IkWGkTR9RG3LFeZ1uJuGmPjhmRFyZwwyz5OV1qui0CPw87A__",
            ),
            Training(
                id = "1",
                instructor = "Intensity",
                title = "Morning stretch",
                time = "15 Min",
                imageUrl = "https://s3-alpha-sig.figma.com/img/5770/d9c0/ad221ffb0e1f7d281b01c5e69c87d26b?Expires=1711324800&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4&Signature=oOGROXkxgMXk07pdG8lTRFGZiCFBTETFD3JZmw3XgFOvsBxnWsVS~sbZ3Kqh-1mJXkwhgEE~eVqH4W~UJ6~j9E5n2cNGOeC811xXjdqg2CImK37P9Cjq-866RGUP9cxFMRGObkxLLWcreu00XA~gCyqArwSyzyW9PW86kHrW4l-mpTrevJxejMc3EP5GWsFOoQ5N7fSxBLAbbhT-4chUZvSIGSachv4ZdXUmDFpB0xR4kXRaxifgXzXmlWR-3qQl-2ctW-3t4j-IxnUa-zOfLrtSrGjd0jvkHCOC6o68WT-GDGUhI87Sm6z6Q6O~vkm0lKy9M8qeeeKGX2edz5~-lA__",
            ),
            Training(
                id = "1",
                instructor = "Intensity",
                title = "Yoga",
                time = "20 Min",
                imageUrl = "https://s3-alpha-sig.figma.com/img/ec15/4c56/dc027cb840412009a1fc07e6b0db7b3c?Expires=1711324800&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4&Signature=RhBckHpwc87MEkauHMGudGzfyMRKnJ~cGdkYpr~2QCcJhhE8z4DU-PzFszbqLyGZ~pSsrjkFi-u7kvU1ahTTdP0fmLas7Uqr0T-~XxnjFJQwCkSzaO0KdfaBbbum3jKSKpB-aBqPwCDTHRAGpIxZUuZC~m1bj0RBrbKhIJnFiw5a9Er5angEjtK1cJia0HYn4sP25HYjzJyxt3tKesC0Df4cn2~QaUb-ZCoPQiTuXZO-LJQgIAXjYqVgiUuCoZnZw~6n0ckX1V01zp8~g7GfaRv1JPMp1mf0SGTLfkDXkn3Mos4kUdhwEIGonuMF~5RP1t9xPqJnXGisstwBxiZqHA__",
            ),
            Training(
                id = "1",
                instructor = "Intensity",
                title = "Wind down",
                time = "16 Min",
                imageUrl = "https://s3-alpha-sig.figma.com/img/9e9e/d62b/548d491bf82b615ac52bb7b20d5370e4?Expires=1711324800&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4&Signature=dJze3SwfE-HrHmeHNtvBinGnwUPxkggi8VJXlOEn3MXWo0DI4SCZhSRkqiCpSvddAN9AdmEQefxW4vmXOAyZ5O7qAzEa6a~jTkWjv8VzLYIGxlOsEJBMeHYTcDi1btqIbBlFw174C3Alg-5qZUWHzMevwGc9G5HXSIRdGFGBRvOTNuOornVdDJyZLUnXnYOXLOw6pggp7kvlvdgqEKEU2AIRzFO-fTuogjQPM6sbwIkbLBoSQl~jiJJ-RxSC05cE3gMop~5GPdkqTqlPErii~OGwVUKdaMl2KvRwHxbaGG4v9ANy9gzfB2ySMLA6AC5c93XUMDaQb0PcjQGe9GMMlg__",
            ),
            Training(
                id = "1",
                instructor = "Intensity",
                title = "Interval 5 day split",
                time = "16 Min",
                imageUrl = "https://s3-alpha-sig.figma.com/img/aad6/dfa7/e5a9a9dd732084d30328dceec998666f?Expires=1711324800&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4&Signature=Sr8NN2cvX3Bc~OmscqwR6VnQhe0Fri9~rM-D~i39wva-lyN6xG1ai7iNXYYa5Kn~uuDhyZ-0BvYVIM~Z2GciW~YMsZzPKyvfnMk92MZ3aVc1ik2fJmOG2SknBSxBuBO0lNRS0HNq-Zx~qe7JFfHC0gadAzzvCH4bB7sI01~HYYQzRjb9EYJdzkd9GkLXD9PaN0iN7in6QRDHSZdgrhT22J96TC3wxRH2mpfFsm3LYA0iSvdZgt6Zj11xrrLlfIvnGVST-SqYnpiv5ixoCHoS18N6Q0KALn7IBH3xk8MSWRnpdfwfjE-CtITEKfOc3YKOCkciHbuhasbdEYyNMHGGcg__",
            ),
            Training(
                id = "1",
                instructor = "Intensity",
                title = "Working in the gym",
                time = "20 Min",
                imageUrl = "https://s3-alpha-sig.figma.com/img/e8c5/e762/261fdaee84066db90f2dcdea8f4b6272?Expires=1711324800&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4&Signature=XpTP2A-yxY8hT91ub7tcc8KAhL7YQUAJnkFCbjU4K8Y3eLt4yqaZzbRmJEtOXKnr0LMWlRPxWafCsLpj79Bgx2lkzI9wl3tKpXI0prvUbl~SwdF5wDR-11A~abdY1ugr4AychRuiweXENGU~fGOvT6HERw2XEE5BD4BafAafDC3J0tNNxoVnG9p612zqMtJKdZpJLOci4O6KOjG51BGA9-Zzat32UtUWGyccUnGkWrPtEoUarnCcNjlJ6mFNYyAWB5GvqDLRF7~ngjVrUj48cF9JFB0OTlys3YGLtxqDsXgFyCeBt9BX8d8x0VVfhsXSd4sjl93FVa70hss9lOxHzA__",
            )
        )
    }

    override suspend fun getUserProfiles(): List<Profile> {
        return listOf(
            Profile(
                id = "1",
                name = "Liam",
                avatar = "https://s3-alpha-sig.figma.com/img/6532/e8f4/1392cf3e62810b5ea04afa65d9b72a51?Expires=1711929600&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4&Signature=R4~LhpixBseeXvgcgkOo87m1tVPmfIsU84BxBo686p-2JdNJc-4niyDJ~4SzLm3f~PKFKtSHVnBoPIFJJEQBBJE3m2UpwdH~-O7FqGIy9lsSdmf82l3vCGPNgX6tLFUkSxD9J4IvwJYd1kYusUhdxVN04M2Zr-O7dT9uUVEvnBrNKSw6taqoGJMDih3Luc8SGfMllrc9uFAM1oKOQJxpkqqzJCj3FpMVzJ5BEuftlcQd1q4PhSp7V9LwPS1n0tcfT02X-NyuBxIWl3wzO8~kpXyqFv83MVqJaGDBugERlGbe5L-vnHhDgybST0SbPSBtAgciKXO4yZpfy9hf5-X57w__"
            ),
            Profile(
                id = "2",
                name = "Olivia",
                avatar = "https://s3-alpha-sig.figma.com/img/3f0e/6a39/508338655b58ecb681612b7c83ffe0bc?Expires=1711929600&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4&Signature=RQuElmLChyZ8-iqnobcT73flYdOez2iSBeGnKKKB1qd67RF4BONYtKHbZWyvaW~W7qPGCykK~82TOeGhSoKACspW9rHYG4nCqG7bD8GCXOCv4MFks5sna~0D6tpWInNmcFxLpHe7v9mHn1GVfa5jq5TMWVk2GWcHNydtMnVr6f1gSiOthkjQ8z3ALjob6WtJlA9RaDuYebNvJ1oxRis6o9QArvO3dknVaQQLnmB2FtoVv-AiWsQoCTiCy6Jp8WiVKF7SGGS-WRd8qQS9YUKY8FHgcpvoAum5Cn6HCksECIsz5oonVuDAyJSil3v9fyG5VnzzE9~IacQUA~E2ZeSFlg__"
            ),
            Profile(
                id = "3",
                name = "Noah",
                avatar = "https://s3-alpha-sig.figma.com/img/3c0b/7630/a6c58e76e1e55007c4ef8859cacb4398?Expires=1711929600&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4&Signature=AlCz1j9iK0OhpkYbrtd1MGytk-1Q9lp51IiF5bkM6ri9Hilgke9REJCZj3eYESxoQLlJEE9LfYmdCPwKHuL5ttaKNjI9Y7R1U88049F8Quy4BIAHo8hV1ZLVp2CoKk9JP7ydMHwicJSwqAyfKokI3GNN1A~GBOoSPX-skdmLx3VK5nleWjtZl4dyxQ77ffnUHLC4lVW55m0Bj5t~3vh6-ZZ6Veu-aOO~r9I9EA4aIiwCLu0HbIN5mal98MXyO2eRqZpvgRffmLHcJPblCWjUhunugwtIaBXbv8GEFM7y0w5F60VhDumHZkhu-ASESrs-3nDCLnV7268z-ifqCat~6Q__"
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
                    imageUrl = "https://s3-alpha-sig.figma.com/img/4a55/976b/4326c161fb1a8e1619b6b935a7d72898?Expires=1711324800&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4&Signature=FrisEgVcxRsPdV5~7TFJuogCRC1DGQncvd7W3eEWrE3raW3WU-NFGMg9-G3rrUAanAM8doc5Ce842G-vEyVzC~eQyY8Sl2X9RJW199oajHOcVq4QBhjWmJBbSiQiJjEm5sqGgyPSUvpWd2D-5b1d7GeSFvRPAnmR-nfnHTlmtGkc3c1y4awXIyWPvzRAxqEwJN~3lsPxAOA~4c7YM5h9tJbM7GbBru~NOdU1cP5tRF52~~H0xgebbcOU1hst5UHvDph-7zsViDPCOWvAJrAwKLF8Jzd1Ts-1BiHsVqFVROTu6eA4pj9t7u7omBGcc0XplFfJobo7YG8pFKJSwKPOrQ__"
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
        return "https://s3-alpha-sig.figma.com/img/4a55/976b/4326c161fb1a8e1619b6b935a7d72898?Expires=1711324800&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4&Signature=FrisEgVcxRsPdV5~7TFJuogCRC1DGQncvd7W3eEWrE3raW3WU-NFGMg9-G3rrUAanAM8doc5Ce842G-vEyVzC~eQyY8Sl2X9RJW199oajHOcVq4QBhjWmJBbSiQiJjEm5sqGgyPSUvpWd2D-5b1d7GeSFvRPAnmR-nfnHTlmtGkc3c1y4awXIyWPvzRAxqEwJN~3lsPxAOA~4c7YM5h9tJbM7GbBru~NOdU1cP5tRF52~~H0xgebbcOU1hst5UHvDph-7zsViDPCOWvAJrAwKLF8Jzd1Ts-1BiHsVqFVROTu6eA4pj9t7u7omBGcc0XplFfJobo7YG8pFKJSwKPOrQ__"
    }
}