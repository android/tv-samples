package com.google.jetfit.data.repositories

import com.google.jetfit.data.entities.Category
import com.google.jetfit.data.entities.Session
import com.google.jetfit.data.entities.Training
import com.google.jetfit.data.entities.Song
import com.google.jetfit.data.entities.Workout
import javax.inject.Inject

class JetFitImpl @Inject constructor() : JetFitRepository {
    override fun getWorkouts() {
        TODO("Not yet implemented")
    }

    override fun getWorkoutById(id: String): Workout {
        return Workout(
            id = "123456sdasdsa",
            title = "Battle ropes HIIT",
            instructor = "Hugo Wright",
            videoUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
            subtitles = null,
            subtitleUri = null
        )
    }

    override fun getWorkoutById() {
        TODO("Not yet implemented")
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
}