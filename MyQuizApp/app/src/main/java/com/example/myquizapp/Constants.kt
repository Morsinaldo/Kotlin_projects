package com.example.myquizapp

object Constants {

    // TODO "Randomize the order of options"

    const val USER_NAME : String = "user_name"
    const val TOTAL_QUESTIONS : String = "total_questions"
    const val CORRECT_ANSWERS: String = "correct_answers"

    fun getQuestions():ArrayList<Question>{
        val questionsList = ArrayList<Question>()

        val q1 = Question(
            1, "What country does this flag belong to?",
            R.drawable.ic_flag_of_argentina,
            "Argentina", "Australia",
            "Armenia", "Austria",
            1
        )
        questionsList.add(q1)

        val q2 = Question(
            1, "What country does this flag belong to?",
            R.drawable.ic_flag_of_australia,
            "Angola", "Austria",
            "Australia", "Armenia",
            3
        )
        questionsList.add(q2)

        val q3 = Question(
            1, "What country does this flag belong to?",
            R.drawable.ic_flag_of_brazil,
            "Balarus", "Belize",
            "Brunei", "Brazil",
            4
        )
        questionsList.add(q3)

        val q4 = Question(
            1, "What country does this flag belong to?",
            R.drawable.ic_flag_of_belgium,
            "Bahamas", "Belgium",
            "Barbados", "Belize",
            2
        )
        questionsList.add(q4)

        val q5 = Question(
            1, "What country does this flag belong to?",
            R.drawable.ic_flag_of_fiji,
            "Gabon", "France",
            "Fiji", "Finland",
            3
        )
        questionsList.add(q5)

        val q6 = Question(
            1, "What country does this flag belong to?",
            R.drawable.ic_flag_of_germany,
            "Germany", "Georgia",
            "Greece", "none of these",
            1
        )
        questionsList.add(q6)

        val q7 = Question(
            1, "What country does this flag belong to?",
            R.drawable.ic_flag_of_denmark,
            "Dominica", "Egypt",
            "Denmark", "Ethiopia",
            3
        )
        questionsList.add(q7)

        val q8 = Question(
            1, "What country does this flag belong to?",
            R.drawable.ic_flag_of_india,
            "Ireland", "Iran",
            "Hungary", "India",
            4
        )
        questionsList.add(q8)

        val q9 = Question(
            9, "What country does this flag belong to?",
            R.drawable.ic_flag_of_new_zealand,
            "New Zealand", "Jordan",
            "Sudan", "Palestine",
            1
        )
        questionsList.add(q9)

        return questionsList
    }
}