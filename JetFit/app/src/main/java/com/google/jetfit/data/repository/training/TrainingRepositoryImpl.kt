package com.google.jetfit.data.repository.training

import com.google.jetfit.data.entities.Training
import com.google.jetfit.data.entities.TrainingDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TrainingRepositoryImpl @Inject constructor(

):TrainingRepository
{
    override suspend fun getTrainingsRecommended(): List<Training> {
        return listOf(
            Training(
                id = "1",
                instructor = "Intensity",
                title = "Full body strength",
                time = "26 Min",
                imageUrl = "https://github.com/TheChance101/tv-samples/assets/45900975/8e91aafe-52be-41fd-8869-977ca0410ab5",
            ),
            Training(
                id = "2",
                instructor = "Intensity",
                title = "Total-body balance pilates",
                time = "24 Min",
                imageUrl = "https://github.com/TheChance101/tv-samples/assets/45900975/41277637-6cc9-4bcd-9a1f-b4b12d501dc8",
            ),
            Training(
                id = "3",
                instructor = "Intensity",
                title = "Circuit training",
                time = "13 Min",
                imageUrl = "https://github.com/TheChance101/tv-samples/assets/45900975/b1773e6c-4a50-4e45-a8f2-5ff176e8167a",
            ),
            Training(
                id = "4",
                instructor = "Intensity",
                title = "Morning stretch",
                time = "15 Min",
                imageUrl = "https://github.com/TheChance101/tv-samples/assets/45900975/7927e3f0-bb09-4309-a11c-0748cd39b535",
            ),
            Training(
                id = "5",
                instructor = "Intensity",
                title = "Yoga",
                time = "20 Min",
                imageUrl = "https://github.com/TheChance101/tv-samples/assets/45900975/194152ea-d535-4504-bcae-17007d47e20a",
            ),
            Training(
                id = "6",
                instructor = "Intensity",
                title = "Wind down",
                time = "16 Min",
                imageUrl = "https://github.com/TheChance101/tv-samples/assets/45900975/f086b517-9c6a-44fa-9924-a6a5c9bde386",
            ),
            Training(
                id = "7",
                instructor = "Intensity",
                title = "Interval 5 day split",
                time = "16 Min",
                imageUrl = "https://github.com/TheChance101/tv-samples/assets/45900975/e83a8d1e-5d78-4ad2-8a42-341251d77e79",
            ),
            Training(
                id = "8",
                instructor = "Intensity",
                title = "Working in the gym",
                time = "20 Min",
                imageUrl = "https://github.com/TheChance101/tv-samples/assets/45900975/7236d183-0be8-4b4a-b6c9-e628e67c9bc3",
            )
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


}