package com.example.myproject

object BiljkeSingleton {

    val biljke: MutableList<Biljka> = mutableListOf(
        Biljka(
            naziv = "Bosiljak (Ocimum basilicum)",
            porodica = "Lamiaceae (usnate)",
            medicinskoUpozorenje = "Može iritati kožu osjetljivu na sunce. Preporučuje se oprezna upotreba pri korištenju ulja bosiljka.",
            medicinskeKoristi = listOf(MedicinskaKorist.SMIRENJE, MedicinskaKorist.REGULACIJAPROBAVE),
            profilOkusa = ProfilOkusaBiljke.BEZUKUSNO,
            jela = listOf("Salata od paradajza", "Punjene tikvice"),
            klimatskiTipovi = listOf(KlimatskiTip.SREDOZEMNA, KlimatskiTip.SUBTROPSKA),
            zemljisniTipovi = listOf(Zemljiste.PJESKOVITO, Zemljiste.ILOVACA)
        ),
        Biljka(
            naziv = "Nana (Mentha spicata)",
            porodica = "Lamiaceae (metvice)",
            medicinskoUpozorenje = "Nije preporučljivo za trudnice, dojilje i djecu mlađu od 3 godine.",
            medicinskeKoristi = listOf(MedicinskaKorist.PROTUUPALNO, MedicinskaKorist.PROTIVBOLOVA),
            profilOkusa = ProfilOkusaBiljke.MENTA,
            jela = listOf("Jogurt sa voćem", "Gulaš"),
            klimatskiTipovi = listOf(KlimatskiTip.SREDOZEMNA, KlimatskiTip.UMJERENA),
            zemljisniTipovi = listOf(Zemljiste.GLINENO, Zemljiste.CRNICA)
        ),
        Biljka(
            naziv = "Kamilica (Matricaria chamomilla)",
            porodica = "Asteraceae (glavočike)",
            medicinskoUpozorenje = "Može uzrokovati alergijske reakcije kod osjetljivih osoba.",
            medicinskeKoristi = listOf(MedicinskaKorist.SMIRENJE, MedicinskaKorist.PROTUUPALNO),
            profilOkusa = ProfilOkusaBiljke.AROMATICNO,
            jela = listOf("Čaj od kamilice"),
            klimatskiTipovi = listOf(KlimatskiTip.UMJERENA, KlimatskiTip.SUBTROPSKA),
            zemljisniTipovi = listOf(Zemljiste.PJESKOVITO, Zemljiste.KRECNJACKO)
        ),
        Biljka(
            naziv = "Ružmarin (Rosmarinus officinalis)",
            porodica = "Lamiaceae (metvice)",
            medicinskoUpozorenje = "Treba ga koristiti umjereno i konsultovati se sa ljekarom pri dugotrajnoj upotrebi ili upotrebi u većim količinama.",
            medicinskeKoristi = listOf(MedicinskaKorist.PROTUUPALNO, MedicinskaKorist.REGULACIJAPRITISKA),
            profilOkusa = ProfilOkusaBiljke.AROMATICNO,
            jela = listOf("Pečeno pile", "Grah","Gulaš"),
            klimatskiTipovi = listOf(KlimatskiTip.SREDOZEMNA, KlimatskiTip.SUHA),
            zemljisniTipovi = listOf(Zemljiste.SLJUNOVITO, Zemljiste.KRECNJACKO)
        ),
        Biljka(
            naziv = "Lavanda (Lavandula angustifolia)",
            porodica = "Lamiaceae (metvice)",
            medicinskoUpozorenje = "Nije preporučljivo za trudnice, dojilje i djecu mlađu od 3 godine. Također, treba izbjegavati kontakt lavanda ulja sa očima.",
            medicinskeKoristi = listOf(MedicinskaKorist.SMIRENJE, MedicinskaKorist.PODRSKAIMUNITETU),
            profilOkusa = ProfilOkusaBiljke.AROMATICNO,
            jela = listOf("Jogurt sa voćem"),
            klimatskiTipovi = listOf(KlimatskiTip.SREDOZEMNA, KlimatskiTip.SUHA),
            zemljisniTipovi = listOf(Zemljiste.PJESKOVITO, Zemljiste.KRECNJACKO)
        ),
        Biljka(
            naziv = "Peršun (Petroselinum crispum)",
            porodica = "Apiaceae (štitarka)",
            medicinskoUpozorenje = "Osobe koje pate od alergija na korijandar, mrkvu ili mašću također mogu biti alergične na peršin.",
            medicinskeKoristi = listOf(MedicinskaKorist.PROTUUPALNO, MedicinskaKorist.REGULACIJAPROBAVE,MedicinskaKorist.SMIRENJE),
            profilOkusa = ProfilOkusaBiljke.LJUTO,
            jela = listOf("Juha od peršuna", "Salata od peršina"),
            klimatskiTipovi = listOf(KlimatskiTip.UMJERENA, KlimatskiTip.SUBTROPSKA),
            zemljisniTipovi = listOf(Zemljiste.PJESKOVITO, Zemljiste.ILOVACA)
        ),
        Biljka(
            naziv = "Kopriva (Urtica dioica)",
            porodica = "Urticaceae (koprivke)",
            medicinskoUpozorenje = "Izbjegavajte dodir s koprivom ako ste alergični na nju.",
            medicinskeKoristi = listOf(MedicinskaKorist.PODRSKAIMUNITETU, MedicinskaKorist.SMIRENJE),
            profilOkusa = ProfilOkusaBiljke.LJUTO,
            jela = listOf("Čaj od koprive", "Pesto od koprive"),
            klimatskiTipovi = listOf(KlimatskiTip.UMJERENA, KlimatskiTip.SUHA),
            zemljisniTipovi = listOf(Zemljiste.SLJUNOVITO, Zemljiste.KRECNJACKO)
        ),
        Biljka(
            naziv = "Matičnjak (Melissa officinalis)",
            porodica = "Lamiaceae (metvice)",
            medicinskoUpozorenje = "Oprez kod osoba s hipotireozom, budući da može sniziti razinu hormona.",
            medicinskeKoristi = listOf(MedicinskaKorist.SMIRENJE, MedicinskaKorist.PODRSKAIMUNITETU),
            profilOkusa = ProfilOkusaBiljke.CITRUSNI,
            jela = listOf("Čaj od matičnjaka", "Koktel s matičnjakom"),
            klimatskiTipovi = listOf(KlimatskiTip.UMJERENA, KlimatskiTip.SREDOZEMNA),
            zemljisniTipovi = listOf(Zemljiste.SLJUNOVITO, Zemljiste.PJESKOVITO)
        ),
        Biljka(
            naziv = "Kamilica (Matricaria chamomilla)",
            porodica = "Asteraceae (glavočike)",
            medicinskoUpozorenje = "Može uzrokovati alergijske reakcije kod osjetljivih osoba.",
            medicinskeKoristi = listOf(MedicinskaKorist.SMIRENJE, MedicinskaKorist.PROTUUPALNO),
            profilOkusa = ProfilOkusaBiljke.AROMATICNO,
            jela = listOf("Čaj od kamilice"),
            klimatskiTipovi = listOf(KlimatskiTip.UMJERENA, KlimatskiTip.SUBTROPSKA),
            zemljisniTipovi = listOf(Zemljiste.PJESKOVITO, Zemljiste.KRECNJACKO)
        ),
        Biljka(
            naziv = "Hibiscus (Hibiscus sabdariffa)",
            porodica = "Malvaceae (sljezovke)",
            medicinskoUpozorenje = "Ne preporučuje se za trudnice i dojilje.",
            medicinskeKoristi = listOf(MedicinskaKorist.SMIRENJE, MedicinskaKorist.PROTUUPALNO),
            profilOkusa = ProfilOkusaBiljke.CITRUSNI,
            jela = listOf("Čaj od hibiskusa", "Hibiskusni sok"),
            klimatskiTipovi = listOf(KlimatskiTip.TROPSKA, KlimatskiTip.SUBTROPSKA),
            zemljisniTipovi = listOf(Zemljiste.ILOVACA, Zemljiste.SLJUNOVITO)
        )
    )
    // Dodavanje nove biljke u listu
    fun dodajNovuBiljku(biljka: Biljka) {
        biljke.add(biljka)
        // Ispisujemo sve biljke u konzolu nakon dodavanja nove biljke
        println("Dodana nova biljka: $biljka")
        println("Sve biljke:")
        biljke.forEach { println(it) }
    }
}