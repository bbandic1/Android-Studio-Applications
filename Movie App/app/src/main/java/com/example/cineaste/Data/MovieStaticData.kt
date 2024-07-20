package com.example.cineaste.Data

fun getFavoriteMovies(): List<Movie> {
    return listOf(
        Movie(1,"Pride and prejudice",
            "Sparks fly when spirited Elizabeth Bennet meets single, rich, and proud Mr. Darcy. But Mr. Darcy reluctantly finds himself falling in love with a woman beneath his class. Can each overcome their own pride and prejudice?",
            "16.02.2005.","https://www.imdb.com/title/tt0414387/",
            "https://www.imdb.com/title/tt0414387/","https://www.imdb.com/title/tt0414387/"),
        Movie(2,"Pulp Fiction",
            "The lives of two mob hitmen, a boxer, a gangster and his wife, and a pair of diner bandits intertwine in four tales of violence and redemption.",
            "14.10.1994.","https://www.imdb.com/title/tt0110912/",
            "https://www.imdb.com/title/tt0414387/","https://www.imdb.com/title/tt0414387/"),
        Movie(3,"The Lord of the Rings",
            "A meek Hobbit from the Shire and eight companions set out on a journey to destroy the powerful One Ring and save Middle-earth from the Dark Lord Sauron.",
            "10.12.2001","https://www.imdb.com/title/tt0120737/",
            "https://www.imdb.com/title/tt0414387/","https://www.imdb.com/title/tt0414387/"),
        Movie(4,"Serenity",
            "The crew of the ship Serenity try to evade an assassin sent to recapture one of their members who is telepathic.",
            "30.09.2005","https://www.imdb.com/title/tt0379786/",
            "https://www.imdb.com/title/tt0414387/","https://www.imdb.com/title/tt0414387/"),
        Movie(5,"Shaun of the Dead",
            "A man's uneventful life is disrupted by the zombie apocalypse.",
            "09.04.2004","https://www.imdb.com/title/tt0365748/",
            "https://www.imdb.com/title/tt0414387/","https://www.imdb.com/title/tt0414387/"),
        Movie(6,"Watchmen",
            "In 1985 where former superheroes exist, the murder of a colleague sends active vigilante Rorschach into his own sprawling investigation, uncovering something that could completely change the course of history as we know it.",
            "23.02.2009.","https://www.imdb.com/title/tt0409459/",
            "https://www.imdb.com/title/tt0414387/","https://www.imdb.com/title/tt0414387/")
    )
}
fun getRecentMovies(): List<Movie> {
    return listOf(
        Movie(1,"Furiosa: A Mad Max Saga",
            "The origin story of renegade warrior Furiosa before her encounter and teamup with Mad Max.",
            "24.05.2024.","https://www.imdb.com/title/tt12037194",
            "https://www.imdb.com/title/tt12037194","https://www.imdb.com/title/tt12037194"),

        Movie(2,"Deadpool & Wolverine",
            "Deadpool and Wolverine reunite.",
            "26.07.2024.","https://www.imdb.com/title/tt6263850",
            "https://www.imdb.com/title/tt12037194","https://www.imdb.com/title/tt12037194"),

        Movie(3,"Despicable Me 4",
            "Gru, Lucy, Margo, Edith, and Agnes welcome a new member to the family, Gru Jr., who is intent on tormenting his dad. ",
            "05.07.2024.","https://www.imdb.com/title/tt7510222",
            "https://www.imdb.com/title/tt12037194","https://www.imdb.com/title/tt12037194"),

        Movie(4,"A Quiet Place: Day One",
            "Experience the day the world went quiet.",
            "28.07.2024.","https://www.imdb.com/title/tt13433802",
            "https://www.imdb.com/title/tt12037194","https://www.imdb.com/title/tt12037194"),

        Movie(5,"Bob Marley: One Love",
            "The story of how reggae icon Bob Marley overcame adversity, and the journey behind his revolutionary music.",
            "14.02.2024.","https://www.imdb.com/title/tt8521778",
            "https://www.imdb.com/title/tt12037194","https://www.imdb.com/title/tt12037194"),
    )
}
fun getMovieActors():Map<String,List<String>>{
    return mapOf<String,List<String>>("Pulp Fiction" to listOf("John Travolta","Samuel L. Jackson","Bruce Willis","Amanda Plummer","Laura Lovelace"),"Pride and prejudice" to listOf("Keira Knightley","Talulah Riley","Rosamund Pike"))
}

fun getSimilarMovies():Map<String,List<String>>{
    return mapOf<String,List<String>>("Pulp Fiction" to listOf("Fight Club","Inception","Se7en"),"Pride and prejudice" to listOf("Jane Eyre","The Notebook","Atonement"))
}