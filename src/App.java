/**
 * App.java
 *
 * Point d'entrée principal pour l'application EcoManager.
 *
 * Auteurs : Yann RENARD, Yanis MEKKI
 */

import extensions.File;
import extensions.CSVFile;

class App extends Program {

    // ============= CONSTANTES =============
    final String NAME = "EcoManager";
    final String TITLE = "./ressources/ascii_title.txt";
    final String RULES = "./ressources/rules.txt";
    final String DECISIONS = "./ressources/decisions.csv";
    final String WIN = "./ressources/win.txt";
    final String LOSE = "./ressources/lose.txt";
    final String EVENT = "./ressources/evenements.csv";

    // Constantes de couleur ANSI
    final String ANSI_RESET = "\u001B[0m";
    final String ANSI_RED = "\u001B[31m";
    final String ANSI_GREEN = "\u001B[32m";
    final String ANSI_YELLOW = "\u001B[33m";
    final String ANSI_BLUE = "\u001B[34m";
    final String ANSI_PURPLE = "\u001B[35m";
    final String ANSI_CYAN = "\u001B[36m";

    int currentSaveIndex = -1;

    // ============= MÉTHODES PRINCIPALES =============
    void algorithm() {
        jeu();
    }

    void jeu(){
        // Initialisation de la partie
        clearScreen();
        Decisions[] listeDecisions = loadDecision(DECISIONS);
        City ville = creerPartie();
        initialiserJeu(ville);
        boolean continuerJeu = true;
        
        // Boucle principale du jeu
        // Continue tant que les conditions de défaite ne sont pas remplies et qu'on n'a pas atteint 30 tours
        while (continuerJeu && ville.tour < 30 && ville.bonheur >= 50 && ville.pollution <= 100 && ville.budget >= 0) {
            clearScreen();
            pourcentageCorrect(ville);
            afficherTxt(TITLE);
            afficherEtatJeu(ville);
            
            Decisions[] decisionsProposees = genererDecisionsAleatoires(listeDecisions);
            afficherDecisions(decisionsProposees);

            int choix = choixValideNbr(5); // Remplacer par botPlay(ville, listeDecisions); pour activer le systeme de bot
            if (choix == 5) {
                sauvegarderPartie(ville);
                continuerJeu = false;
            } else {
                appliquerDecision(ville, choix, decisionsProposees[0], decisionsProposees[1], 
                                decisionsProposees[2], decisionsProposees[3]);
            }
            eventRandom(ville);
        }
        
        if (!continuerJeu) {
            jeu(); // Retour au menu principal
        } else {
            gererFinDePartie(ville); // Gestion de la victoire/défaite
        }
    }

    // ============= INITIALISATION ET MENU =============
    void initialiserJeu(City ville) {
        clearScreen();
        afficherTxt(TITLE);
        afficherMenuStart();
        int choix = choixValideNbr(4);
        startSelect(choix, ville);
    }

    void afficherMenuStart() {
        println(ANSI_CYAN + "Choisissez une option :" + ANSI_RESET);
        println(ANSI_YELLOW + "1. " + ANSI_RESET + "Nouvelle ville");
        println(ANSI_YELLOW + "2. " + ANSI_RESET + "Charger une partie");
        println(ANSI_YELLOW + "3. " + ANSI_RESET + "Afficher les règles du jeu");
        println(ANSI_YELLOW + "4. " + ANSI_RESET + "Quitter");
    }

    void startSelect(int choix, City ville) {
        clearScreen();
        afficherTxt(TITLE);
        if (choix == 1) {
            println("Quel est le nom de votre ville ?");
            print("> ");
            String nom = readString();
            if (nom == null || length(nom) == 0) {
                nom = "<default>";
            }
            ville.nom = nom;
        } else if (choix == 2) {
            chargerPartie(ville);
        } else if (choix == 3) {
            afficherTxt(RULES);
            choixDeQuitter();
            initialiserJeu(ville);
        } else if (choix == 4) {
            System.exit(0);
        }
    }

    // ============= GESTION DU JEU =============
    void gererFinDePartie(City ville) {
        if (ville.bonheur < 50 || ville.pollution > 100 || ville.budget < 0) {
            afficherTxt(LOSE);
            if (sauvegardeExiste(ville.nom)) {
                supprimerSauvegarde(ville.nom);
            }
            delay(10000);
            jeu();
        } else if (ville.tour >= 30) {
            afficherTxt(WIN);
            if (sauvegardeExiste(ville.nom)) {
                supprimerSauvegarde(ville.nom);
            }
            delay(10000);
            jeu();
        } else {
            sauvegarderPartie(ville);
            jeu();
        }
    }

    boolean sauvegardeExiste(String nomVille) {
        CSVFile existingSaves = loadCSV("ressources/save.csv");
        int existingRows = rowCount(existingSaves);
        
        for (int i = 0; i < existingRows; i++) {
            if (getCell(existingSaves, i, 0).equals(nomVille)) {
                return true;
            }
        }
        return false;
    }

    void appliquerDecision(City ville, int choix, Decisions num1, Decisions num2, Decisions num3, Decisions num4) {
        Decisions decisionChoisie = null;
        
        // Assigner la décision en fonction du choix du joueur
        if (choix == 1) {
            decisionChoisie = num1;  // Première option
        } else if (choix == 2) {
            decisionChoisie = num2;  // Deuxième option
        } else if (choix == 3) {
            decisionChoisie = num3;  // Troisième option
        } else if (choix == 4) {
            decisionChoisie = num4;  // Quatrième option
        }
        
        if (decisionChoisie != null) {
            ville.tour++;
            ville.budget += decisionChoisie.argent;
            ville.pollution += decisionChoisie.pollution;
            ville.bonheur += decisionChoisie.bonheur;
            
            // Ajout d'un bonus d'argent à chaque tour
            ville.budget += 1500; // Bonus de 1500 € par tour
            println(decisionChoisie.message);
            pourcentageCorrect(ville);
        }
    }

    // ============= INTERFACE UTILISATEUR =============
    int choixValideNbr(int nbrChoix) {
        println("- - - - - - - - - - - - - - - - -");
        print("Choisissez une action (1-" + nbrChoix + ") : ");
        
        while (true) {
            String input = readString();
            if (input != null && length(input) > 0) {
                int choix = (int)charAt(input, 0) - '0';
                if (choix >= 1 && choix <= nbrChoix) {
                    return choix;
                }
            }
            println("La saisie est invalide !");
            delay(2000);
            print("Choisissez une action (1-" + nbrChoix + ") : ");
        }
    }

    char choixDeQuitter() {
        println("- - - - - - - - - - - - - - - - -");
        println("Pour quitter la page de règles du jeu, appuyez sur 'q'");
        char choixQ = readChar();
        while (choixQ != 'q') {
            println("La saisie est invalide, veuillez recommencer !");
            choixQ = readChar();
        }
        return choixQ;
    }

    void afficherEtatJeu(City ville) {
        println("Nom de la ville");
        println("╰┈➤ " + ANSI_CYAN + ville.nom + ANSI_RESET);
        println("---------------------------------");
        println(" ❈ Tour : " + ANSI_YELLOW + ville.tour + ANSI_RESET);
        println(" ❈ Budget : " + ANSI_GREEN + ville.budget + " €" + ANSI_RESET);
        println(" ❈ Pollution : " + ANSI_PURPLE + ville.pollution + "%" + ANSI_RESET);
        println(" ❈ Bonheur : " + ANSI_BLUE + ville.bonheur + "%" + ANSI_RESET);
        println("---------------------------------");
    }

    void afficherTxt(String dessin) {
        File file = newFile(dessin);
        while (ready(file)) {
            println(readLine(file));
        }
    }

    void afficherDecisions(Decisions[] decisions) {
        for (int i = 0; i < 4; i++) {
            println(ANSI_YELLOW + (i+1) + ". " + ANSI_RESET + decisions[i].desc + " (" + 
                    ANSI_GREEN + decisions[i].argent + " €" + ANSI_RESET + ", " + 
                    ANSI_PURPLE + decisions[i].pollution + " pollution" + ANSI_RESET + ", " + 
                    ANSI_BLUE + decisions[i].bonheur + " bonheur" + ANSI_RESET + ")");
        }
        println("- - - - - - - - - - - - - - - - - - -");
        println(ANSI_PURPLE + "Pour revenir au menu et sauvegarder, entrez 5" + ANSI_RESET);
    }

    // ============= GESTION DES DONNÉES =============
    City creerPartie() {
        City ville = new City();
        ville.nom = "default";
        ville.tour = 1;
        ville.budget = 45000;
        ville.pollution = 20;
        ville.bonheur = 85;
        return ville;
    }

    void pourcentageCorrect(City ville) {
        if (ville.bonheur >= 100) {
            ville.bonheur = 100;
        }
        if (ville.pollution <= 0) {
            ville.pollution = 0;
        }
    }

    // ============= GESTION DES DÉCISIONS =============
    Decisions newDecisions(String nom, String desc, int argent, int pollution, int bonheur, String message) {
        Decisions decision = new Decisions();
        decision.nom = nom;
        decision.desc = desc;
        decision.argent = argent;
        decision.pollution = pollution;
        decision.bonheur = bonheur;
        decision.message = message;
        return decision;
    }

    Decisions[] loadDecision(String nomFile) {
        CSVFile deciAsString = loadCSV(nomFile);
        Decisions[] decisions = new Decisions[rowCount(deciAsString) - 1];
        for (int idxD = 1; idxD < length(decisions) + 1; idxD++) {
            String nom = getCell(deciAsString, idxD, 0);
            String desc = getCell(deciAsString, idxD, 1);
            int argent = stringToInt(getCell(deciAsString, idxD, 2));
            int pollution = stringToInt(getCell(deciAsString, idxD, 3));
            int bonheur = stringToInt(getCell(deciAsString, idxD, 4));
            String message = getCell(deciAsString, idxD, 5);
            decisions[idxD - 1] = newDecisions(nom, desc, argent, pollution, bonheur, message);
        }
        return decisions;
    }

    Decisions[] genererDecisionsAleatoires(Decisions[] listeDecisions) {
        Decisions[] decisions = new Decisions[4];
        for (int i = 0; i < 4; i++) {
            decisions[i] = listeDecisions[(int) (random() * 73)];
        }
        return decisions;
    }

    // ============= GESTION DES ÉVÉNEMENTS =============
    Evenements newEvenements(String nom, String desc, int argent, int pollution, int bonheur) {
        Evenements evenement = new Evenements();
        evenement.nom = nom;
        evenement.desc = desc;
        evenement.argent = argent;
        evenement.pollution = pollution;
        evenement.bonheur = bonheur;
        return evenement;
    }

    Evenements[] loadEvenements(String nomFile) {
        CSVFile eventAsString = loadCSV(nomFile);
        Evenements[] evenements = new Evenements[rowCount(eventAsString) - 1];
        for (int idxE = 1; idxE < length(evenements) + 1; idxE++) {
            String nom = getCell(eventAsString, idxE, 0);
            String desc = getCell(eventAsString, idxE, 1);
            int argent = stringToInt(getCell(eventAsString, idxE, 2));
            int pollution = stringToInt(getCell(eventAsString, idxE, 3));
            int bonheur = stringToInt(getCell(eventAsString, idxE, 4));
            evenements[idxE - 1] = newEvenements(nom, desc, argent, pollution, bonheur);
        }
        return evenements;
    }

    void eventRandom(City ville) {
        Evenements[] listeEvent = loadEvenements(EVENT);
        // 20% de chance qu'un événement aléatoire se produise après le premier tour
        int nb = tirerAuHasard(100);
        if (nb <= 20 && ville.tour > 1) {
            // Sélection aléatoire d'un événement et application de ses effets
            Evenements event = listeEvent[(int) (random() * length(listeEvent))];
            println(event.desc);
            // Application des modifications négatives (d'où les signes -)
            ville.budget -= event.argent;
            ville.pollution -= event.pollution;
            ville.bonheur -= event.bonheur;
            pourcentageCorrect(ville);
            
            afficherImpactEvenement(event);
            delay(3000);
            clearScreen();
        }
    }

    void afficherImpactEvenement(Evenements event) {
        // Utilisation de couleurs pour les messages
        if (event.argent >= 0) {
            println(ANSI_GREEN + "Vous avez gagné " + event.argent + "$ !" + ANSI_RESET);
        } else {
            println(ANSI_RED + "Vous avez perdu " + (-event.argent) + "$ !" + ANSI_RESET);
        }
        if (event.bonheur >= 0) {
            println(ANSI_GREEN + "Vous avez gagné " + event.bonheur + "% de bonheur !" + ANSI_RESET);
        } else {
            println(ANSI_RED + "Vous avez perdu " + (-event.bonheur) + "% de bonheur !" + ANSI_RESET);
        }
        if (event.pollution >= 0) {
            println(ANSI_GREEN + "Vous avez gagné " + event.pollution + "% de pollution !" + ANSI_RESET);
        } else {
            println(ANSI_RED + "Vous avez perdu " + (-event.pollution) + "% de pollution !" + ANSI_RESET);
        }
    }

    // ============= SYSTÈME DE BOT =============
    int botPlay(City ville, Decisions[] decisions) {
        // Choix aléatoire entre 1 et 4
        int choix = tirerAuHasard(4) + 1; // tirerAuHasard() retourne un nombre entre 0 et 3, donc on ajoute 1 pour obtenir un nombre entre 1 et 4
        
        // Log du choix du bot
        println("Le bot a choisi l'option " + choix);
        
        // Ajout d'un délai avant de retourner le choix (sinon on ne voit rien et ce n'est pas pratique)
        delay(1500); 

        return choix;
    }

    // ============= GESTION DES SAUVEGARDES =============
    void sauvegarderPartie(City ville) {
        CSVFile existingSaves = loadCSV("ressources/save.csv");
        int existingRows = rowCount(existingSaves);
        
        // Recherche si une sauvegarde existe déjà pour cette ville
        currentSaveIndex = -1;
        for (int i = 0; i < existingRows && currentSaveIndex == -1; i++) {
            if (getCell(existingSaves, i, 0).equals(ville.nom)) {
                currentSaveIndex = i;
            }
        }

        String[][] contenu = creerContenuSauvegarde(existingSaves, existingRows, currentSaveIndex);
        mettreAJourSauvegarde(contenu, currentSaveIndex, ville);
        
        saveCSV(contenu, "ressources/save.csv");
        println("Partie sauvegardée !");
    }

    String[][] creerContenuSauvegarde(CSVFile existingSaves, int existingRows, int currentSaveIndex) {
        // Si des sauvegardes existent déjà
        if (existingRows > 0) {
            // Création d'un nouveau tableau avec une ligne supplémentaire si c'est une nouvelle sauvegarde
            // ou de même taille si on écrase une sauvegarde existante
            String[][] contenu = new String[currentSaveIndex == -1 ? existingRows + 1 : existingRows][5];
            for (int i = 0; i < existingRows; i++) {
                for (int j = 0; j < 5; j++) {
                    contenu[i][j] = getCell(existingSaves, i, j);
                }
            }
            return contenu;
        }
        return new String[1][5];
    }

    void mettreAJourSauvegarde(String[][] contenu, int index, City ville) {
        if (index == -1) index = length(contenu) - 1;
        contenu[index][0] = ville.nom;
        contenu[index][1] = "" + ville.tour;
        contenu[index][2] = "" + ville.budget;
        contenu[index][3] = "" + ville.pollution;
        contenu[index][4] = "" + ville.bonheur;
    }

    void chargerPartie(City ville) {
        CSVFile saveFile = loadCSV("ressources/save.csv");
        int totalSaves = rowCount(saveFile);

        if (totalSaves == 0) {
            println("Aucune sauvegarde trouvée.");
            return;
        }

        afficherSauvegardes(saveFile, totalSaves);
        int choix = choixValideNbr(totalSaves);
        chargerDonnees(ville, saveFile, choix - 1);
    }

    void afficherSauvegardes(CSVFile saveFile, int totalSaves) {
        println("Parties sauvegardées :");
        for (int i = 0; i < totalSaves; i++) {
            println((i + 1) + ". " + getCell(saveFile, i, 0));
        }
    }

    void chargerDonnees(City ville, CSVFile saveFile, int index) {
        currentSaveIndex = index;
        ville.nom = getCell(saveFile, currentSaveIndex, 0);
        ville.tour = stringToInt(getCell(saveFile, currentSaveIndex, 1));
        ville.budget = stringToInt(getCell(saveFile, currentSaveIndex, 2));
        ville.pollution = stringToInt(getCell(saveFile, currentSaveIndex, 3));
        ville.bonheur = stringToInt(getCell(saveFile, currentSaveIndex, 4));
        println("Partie chargée !");
    }

    void supprimerSauvegarde(String nomVille) {
        CSVFile existingSaves = loadCSV("ressources/save.csv");
        int existingRows = rowCount(existingSaves);
        
        // Vérifiez s'il y a des sauvegardes avant de tenter de les supprimer
        if (existingRows == 0) {
            println("Aucune sauvegarde à supprimer.");
            return;
        }

        String[][] nouveauContenu = new String[existingRows - 1][5];
        int nouvelIndex = 0;
        
        for (int i = 0; i < existingRows; i++) {
            if (!getCell(existingSaves, i, 0).equals(nomVille)) {
                for (int j = 0; j < 5; j++) {
                    nouveauContenu[nouvelIndex][j] = getCell(existingSaves, i, j);
                }
                nouvelIndex++;
            }
        }
        
        saveCSV(nouveauContenu, "ressources/save.csv");
    }

    // ============= UTILITAIRES =============
    int tirerAuHasard(int max) {
        return (int) (random() * max);
    }

    // ============= TESTS =============
    void testCreerPartie() {
        City ville = creerPartie();
        assertEquals("default", ville.nom);
        assertEquals(1, ville.tour);
        assertEquals(45000, ville.budget);
        assertEquals(20, ville.pollution);
        assertEquals(85, ville.bonheur);
    }

    void testPourcentageCorrect() {
        City ville = new City();
        ville.bonheur = 120;
        ville.pollution = -10;
        pourcentageCorrect(ville);
        assertEquals(100, ville.bonheur);
        assertEquals(0, ville.pollution);
    }

    void testNewDecisions() {
        Decisions decision = newDecisions("test", "description", 1000, 10, 5, "message test");
        assertEquals("test", decision.nom);
        assertEquals("description", decision.desc);
        assertEquals(1000, decision.argent);
        assertEquals(10, decision.pollution);
        assertEquals(5, decision.bonheur);
        assertEquals("message test", decision.message);
    }

    void testNewEvenements() {
        Evenements event = newEvenements("test", "description", 1000, 10, 5);
        assertEquals("test", event.nom);
        assertEquals("description", event.desc);
        assertEquals(1000, event.argent);
        assertEquals(10, event.pollution);
        assertEquals(5, event.bonheur);
    }

    void testTirerAuHasard() {
        int max = 100;
        for (int i = 0; i < 1000; i++) {
            int result = tirerAuHasard(max);
            assertTrue(result >= 0 && result < max);
        }
    }

    void testGenererDecisionsAleatoires() {
        Decisions[] listeDecisions = new Decisions[73];
        for (int i = 0; i < 73; i++) {
            listeDecisions[i] = newDecisions("test" + i, "desc", 0, 0, 0, "");
        }
        
        Decisions[] result = genererDecisionsAleatoires(listeDecisions);
        assertEquals(4, length(result));
        for (int i = 0; i < 4; i++) {
            assertTrue(length(result[i].nom) > 0);
        }
    }

    void testAppliquerDecision() {
        City ville = creerPartie();
        Decisions d1 = newDecisions("test1", "desc1", 1000, 5, 10, "message1");
        Decisions d2 = newDecisions("test2", "desc2", -500, -3, -5, "message2");
        Decisions d3 = newDecisions("test3", "desc3", 200, 2, 3, "message3");
        Decisions d4 = newDecisions("test4", "desc4", -100, -1, -2, "message4");

        // Test choix 1
        int tourInitial = ville.tour;
        int budgetInitial = ville.budget;
        int pollutionInitiale = ville.pollution;
        int bonheurInitial = ville.bonheur;
        
        appliquerDecision(ville, 1, d1, d2, d3, d4);
        
        assertEquals(tourInitial + 1, ville.tour);
        assertEquals(budgetInitial + d1.argent, ville.budget);
        assertEquals(pollutionInitiale + d1.pollution, ville.pollution);
        assertEquals(bonheurInitial + d1.bonheur, ville.bonheur);
    }

    void testChoixValideNbr() {
        // La fonction nécessite une entrée utilisateur donc le test devra être simulé avec des entrées prédéfinies
    }

    void testAfficherEtatJeu() {
        City ville = new City();
        ville.nom = "TestVille";
        ville.tour = 5;
        ville.budget = 50000;
        ville.pollution = 30;
        ville.bonheur = 75;
    }

    void testLoadDecision() {
        Decisions[] decisions = loadDecision(DECISIONS);
        assertTrue(length(decisions) > 0);
        
        // Vérifier le premier élément n'est pas vide
        assertTrue(length(decisions[0].nom) > 0);
        assertTrue(length(decisions[0].desc) > 0);
        assertTrue(length(decisions[0].message) > 0);
    }

    void testLoadEvenements() {
        Evenements[] evenements = loadEvenements(EVENT);
        assertTrue(length(evenements) > 0);
        
        // Vérifier que le premier élément n'est pas vide
        assertTrue(length(evenements[0].nom) > 0);
        assertTrue(length(evenements[0].desc) > 0);
    }

    void testEventRandom() {
        City ville = new City();
        ville.tour = 2;
        ville.budget = 50000;
        ville.pollution = 50;
        ville.bonheur = 75;
        
        // Exécuter plusieurs fois pour tester la probabilité
        int eventCount = 0;
        for (int i = 0; i < 1000; i++) {
            City testVille = new City();
            testVille.tour = 2;
            testVille.budget = 50000;
            testVille.pollution = 50;
            testVille.bonheur = 75;
            
            eventRandom(testVille);
            if (testVille.budget != 50000 || testVille.pollution != 50 || testVille.bonheur != 75) {
                eventCount++;
            }
        }
        
        // Vérifier que le taux d'événements est proche de 20%
        assertTrue(eventCount > 150);
        assertTrue(eventCount < 250);
    }

    void testChoisirMeilleurOption() {
        Decisions[] decisions = new Decisions[4];
        decisions[0] = newDecisions("d1", "desc1", 1000, 10, 5, "");
        decisions[1] = newDecisions("d2", "desc2", 2000, 5, 10, "");
        decisions[2] = newDecisions("d3", "desc3", 500, 15, 15, "");
        decisions[3] = newDecisions("d4", "desc4", 1500, 8, 8, "");

        // Test pour le budget
        assertEquals(2, choisirMeilleurOption(decisions, "budget"));
        
        // Test pour la pollution (plus petit = meilleur)
        assertEquals(2, choisirMeilleurOption(decisions, "pollution"));
        
        // Test pour le bonheur
        assertEquals(3, choisirMeilleurOption(decisions, "bonheur"));
    }

    void testSauvegarderEtChargerPartie() {
        // Créer une ville test
        City ville = new City();
        ville.nom = "VilleTest";
        ville.tour = 5;
        ville.budget = 50000;
        ville.pollution = 30;
        ville.bonheur = 75;

        // Sauvegarder
        sauvegarderPartie(ville);

        // Créer une nouvelle ville pour le chargement
        City villeChargee = new City();
        chargerDonnees(villeChargee, loadCSV("ressources/save.csv"), 0);

        // Vérifier que les données sont identiques
        assertEquals(ville.nom, villeChargee.nom);
        assertEquals(ville.tour, villeChargee.tour);
        assertEquals(ville.budget, villeChargee.budget);
        assertEquals(ville.pollution, villeChargee.pollution);
        assertEquals(ville.bonheur, villeChargee.bonheur);

        // Nettoyer après le test
        supprimerSauvegarde("VilleTest");
    }

    void testSupprimerSauvegarde() {
        // Créer une sauvegarde test
        City ville = new City();
        ville.nom = "VilleASupprimer";
        sauvegarderPartie(ville);

        // Supprimer la sauvegarde
        supprimerSauvegarde("VilleASupprimer");

        // Vérifier que la sauvegarde n'existe plus
        CSVFile saves = loadCSV("ressources/save.csv");
        boolean found = false;
        for (int i = 0; i < rowCount(saves); i++) {
            if (equals(getCell(saves, i, 0), "VilleASupprimer")) {
                found = true;
            }
        }
        assertFalse(found);
    }
}
