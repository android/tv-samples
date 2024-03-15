package com.google.jetfit.data.repositories

import com.google.jetfit.data.entities.Challenge
import com.google.jetfit.data.entities.Intensity
import com.google.jetfit.data.entities.Language
import com.google.jetfit.data.entities.Routine
import com.google.jetfit.data.entities.Series
import com.google.jetfit.data.entities.SubtitleLanguage
import com.google.jetfit.data.entities.Workout
import com.google.jetfit.data.entities.WorkoutType
import java.util.Date
import javax.inject.Inject

class JetFitImpl @Inject constructor() : JetFitRepository {
    override fun getWorkouts(): List<Workout> = listOf(
        Workout(
            "1",
            "Back and leg strength",
            "Strong legs do more than look good. Even the simplest daily movements like walking require leg strength. But you may wonder where to begin. Let’s dive in.",
            "Charlotte Aldridge",
            WorkoutType.STRENGTH,
            "https://s3-alpha-sig.figma.com/img/5b76/28da/51c6b4c0076ea7b92c70d82dc1828425?Expires=1710720000&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4&Signature=M1NOL3tptpUBJ4qM0QbqmkCQBAKjWpNw3llpK70HUmcVyUPF9StrimFkFA32ziyk-X8GQz8VJHrT42ovbtj3ROiDwLBbLfpCbkuNaThYT5D0BAVkRZtSjkp~w3yDLQKdRSWp~1pn242mMj5ASFpYjL9udDM4JBHn9gjvzST7QGzvOHes9ZABFtimxVBC0Ot-eQDpDV7mbU9Pf5ROC2JTEd2LK-QG85N0Vv8cIFpUcPJGSFgR1tbHxMDv1GpKAx33eSGnH02~ow3R6sZm88wznn0AaPJoKwGGvU2ZJUVl6wbUD4JRt9gcs3q9FVFFEhSeoOpYbJSdqgWdzhPM-Lv-7Q__",
            30L,
            "https://example.com/video.mp4",
            Intensity.HARD,
            Date(),
            Language.ENGLISH,
            SubtitleLanguage.ENGLISH
        ),
        Workout(
            "2",
            "Morning Yoga Flow",
            "Start your day with an energizing yoga routine. This flow focuses on awakening the body and mind, helping you feel refreshed and ready to tackle the day ahead.",
            "Emma Smith",
            WorkoutType.YOGA,
            "https://s3-alpha-sig.figma.com/img/aad6/dfa7/e5a9a9dd732084d30328dceec998666f?Expires=1711324800&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4&Signature=Sr8NN2cvX3Bc~OmscqwR6VnQhe0Fri9~rM-D~i39wva-lyN6xG1ai7iNXYYa5Kn~uuDhyZ-0BvYVIM~Z2GciW~YMsZzPKyvfnMk92MZ3aVc1ik2fJmOG2SknBSxBuBO0lNRS0HNq-Zx~qe7JFfHC0gadAzzvCH4bB7sI01~HYYQzRjb9EYJdzkd9GkLXD9PaN0iN7in6QRDHSZdgrhT22J96TC3wxRH2mpfFsm3LYA0iSvdZgt6Zj11xrrLlfIvnGVST-SqYnpiv5ixoCHoS18N6Q0KALn7IBH3xk8MSWRnpdfwfjE-CtITEKfOc3YKOCkciHbuhasbdEYyNMHGGcg__",
            30L,
            "https://example.com/video.mp4",
            Intensity.EASY,
            Date(),
            Language.ENGLISH,
            SubtitleLanguage.ENGLISH
        ),
        Workout(
            "3",
            "High-Intensity Interval Training",
            "Experience an intense workout designed to maximize calorie burn and boost metabolism. This high-energy session combines bursts of intense exercise with short recovery periods for maximum results.",
            "John Doe",
            WorkoutType.CARDIO,
            "https://s3-alpha-sig.figma.com/img/5b76/28da/51c6b4c0076ea7b92c70d82dc1828425?Expires=1710720000&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4&Signature=M1NOL3tptpUBJ4qM0QbqmkCQBAKjWpNw3llpK70HUmcVyUPF9StrimFkFA32ziyk-X8GQz8VJHrT42ovbtj3ROiDwLBbLfpCbkuNaThYT5D0BAVkRZtSjkp~w3yDLQKdRSWp~1pn242mMj5ASFpYjL9udDM4JBHn9gjvzST7QGzvOHes9ZABFtimxVBC0Ot-eQDpDV7mbU9Pf5ROC2JTEd2LK-QG85N0Vv8cIFpUcPJGSFgR1tbHxMDv1GpKAx33eSGnH02~ow3R6sZm88wznn0AaPJoKwGGvU2ZJUVl6wbUD4JRt9gcs3q9FVFFEhSeoOpYbJSdqgWdzhPM-Lv-7Q__",
            45L,
            "https://example.com/video.mp4",
            Intensity.HARD,
            Date(),
            Language.ENGLISH,
            SubtitleLanguage.ENGLISH
        ),
        Workout(
            "4",
            "Strength Training for Beginners",
            "Discover the fundamentals of strength training with this beginner-friendly workout. Learn proper form and technique for key exercises to build a strong foundation for future progress.",
            "Maria Garcia",
            WorkoutType.STRENGTH,
            "https://example.com/strength_training.jpg",
            40L,
            "https://example.com/video.mp4",
            Intensity.MEDIUM,
            Date(),
            Language.ENGLISH,
            SubtitleLanguage.SPANISH
        ),
        Workout(
            "5",
            "Relaxing Evening Stretch",
            "Unwind and release tension with this gentle evening stretch routine. Focus on deep breathing and gentle stretches to soothe tired muscles and promote relaxation, preparing you for a restful night's sleep.",
            "David Lee",
            WorkoutType.YOGA,
            "https://example.com/evening_stretch.jpg",
            20L,
            "https://example.com/video.mp4",
            Intensity.EASY,
            Date(),
            Language.ENGLISH,
            SubtitleLanguage.ENGLISH
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

    private fun generateWeeklyPlans(availableWorkouts: List<Workout>): List<Pair<String, List<Workout>>> {
        val shuffledWorkouts = availableWorkouts.shuffled().take(5)
        return listOf(
            "Week 1: Meet the basics" to shuffledWorkouts.shuffled(),
            "Week 2: Get your footing" to shuffledWorkouts.shuffled(),
            "Week 3: Expand your style" to shuffledWorkouts.shuffled(),
            "Week 4: Challenge yourself" to shuffledWorkouts.shuffled()
        )
    }

    override fun getChallengeById(id: String): Challenge = getChallenges().first { it.id == id}
}