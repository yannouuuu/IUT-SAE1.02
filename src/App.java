/**
 * App.java
 *
 * Point d'entrée principal pour l'application EcoManager.
 *
 * Auteurs : Yann RENARD, Yanis MEKKI
 */

// TODO 
// [] Faire quelques tests
// [x] Systeme sauvegarde
// [x] Systeme de chargement de sauvegarde
// [x] Systeme de bot
// [x] Fix la saisie 5 qui renvoie 'la saisie est invalide' 

import extensions.File;
import extensions.CSVFile;

class App extends Program {

    final String NOM = "EcoManager";
    final String TITRE = "./ressources/titre_ascii.txt";
    final String RULES = "./ressources/rules.txt";
    final String DECISIONS = "./ressources/decisions.csv";
    final String WIN = "./ressources/win.txt";
    final String LOSE = "./ressources/lose.txt";
    final String EVENT = "./ressources/evenements.csv";

    int currentSaveIndex = -1;

    void algorithm() {
        jeu();
    }

    void initialiserJeu(City ville) {
        clearScreen();
        afficherTxt(TITRE);
        afficherMenuStart();
        int choix = choixValideNbr(4);
        startSelect(choix, ville);
    }
    void jeu(){
        clearScreen();
        Decisions[] listeDecisions = loadDecision(DECISIONS);
        City ville = creerPartie();
        initialiserJeu(ville);
        boolean continuerJeu = true;
        
        while (continuerJeu && ville.tour < 30 && ville.bonheur >= 50 && ville.pollution <= 100 && ville.budget >= 0) {
            clearScreen();
            pourcentageCorrect(ville);
            afficherTxt(TITRE);
            eventRandom(ville);
            afficherEtatJeu(ville);
            
            Decisions[] decisionsProposees = genererDecisionsAleatoires(listeDecisions);
            afficherDecisions(decisionsProposees);

            int choix = choixValideNbr(5); // Remplacer par botPlay(ville ); pour activer le systeme de bot
            if (choix == 5) {
                sauvegarderPartie(ville);
                continuerJeu = false;
            } else {
                appliquerDecision(ville, choix, decisionsProposees[0], decisionsProposees[1], 
                                decisionsProposees[2], decisionsProposees[3]);
            }
        }
        
        if (!continuerJeu) {
            jeu(); // Retour au menu principal
        } else {
            gererFinDePartie(ville); // Gestion de la victoire/défaite
        }
    }

    // Fonction affichant le menu
    void afficherMenuStart() {
        println("Choisissez une option :");
        println("1. Nouvelle ville");
        println("2. Charger une partie");
        println("3. Afficher les règles du jeu");
        println("4. Quitter");
    }

    // Fonction de la selection des choix du menu
    void startSelect(int choix, City ville) {
        if (choix == 1) {
            println("Quel est le nom de votre ville ?");
            print("> ");
            String nom = readString();
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

    void gererFinDePartie(City ville){
        if (ville.bonheur < 50 || ville.pollution > 100 || ville.budget < 0) {
            afficherTxt(LOSE);
            supprimerSauvegarde(ville.nom);
            delay(10000);
            jeu();
        } else if (ville.tour >= 30) {
            afficherTxt(WIN);
            supprimerSauvegarde(ville.nom);
            delay(10000);
            jeu();
        } else {
            sauvegarderPartie(ville);
            jeu();
        }
    }

    void appliquerDecision(City ville, int choix, Decisions num1, Decisions num2, Decisions num3, Decisions num4) {
        if (choix == 1) {
            ville.tour++;
            ville.budget = ville.budget + num1.argent;
            ville.pollution = ville.pollution + num1.pollution;
            ville.bonheur = ville.bonheur + num1.bonheur;
            println(num1.message);
            pourcentageCorrect(ville);
        } else if (choix == 2) {
            ville.tour++;
            ville.budget = ville.budget + num2.argent;
            ville.pollution = ville.pollution + num2.pollution;
            ville.bonheur = ville.bonheur + num2.bonheur;
            println(num2.message);
            pourcentageCorrect(ville);
        } else if (choix == 3) {
            ville.tour++;
            ville.budget = ville.budget + num3.argent;
            ville.pollution = ville.pollution + num3.pollution;
            ville.bonheur = ville.bonheur + num3.bonheur;
            println(num3.message);
            pourcentageCorrect(ville);
        } else if (choix == 4) {
            ville.tour++;
            ville.budget = ville.budget + num4.argent;
            ville.pollution = ville.pollution + num4.pollution;
            ville.bonheur = ville.bonheur + num4.bonheur;
            println(num4.message);
            pourcentageCorrect(ville);
        }
    }

    int choixValideNbr(int nbrChoix) {
        println("- - - - - - - - - - - - - - - - -");
        print("Choisissez une action (1-" + nbrChoix + ") : ");
        
        while (true) {
            String input = readString();
            // Vérifie si l'entrée n'est pas vide
            if (input != null && length(input) > 0) {
                // Convertit le caractère en ASCII
                int choix = (int)charAt(input, 0) - '0';
                
                // Vérifie si le choix est dans la plage valide
                if (choix >= 1 && choix <= nbrChoix) {
                    return choix;
                }
            }
            println("La saisie est invalide !");
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
        println("╰┈➤ " + ville.nom);
        println("---------------------------------");
        println(" ❈ Tour : " + ville.tour);
        println(" ❈ Budget : " + ville.budget + " €");
        println(" ❈ Pollution : " + ville.pollution + "%");
        println(" ❈ Bonheur : " + ville.bonheur + "%");
        println("---------------------------------");
    }

    void afficherTxt(String dessin) {
        File file = newFile(dessin);
        while (ready(file)) {
            println(readLine(file));
        }
    }

    City creerPartie() {
        City ville = new City();
        ville.nom = "default";
        ville.tour = 1;
        ville.budget = 45000;
        ville.pollution = 20;
        ville.bonheur = 85;
        return ville;
    }

    int tirerAuHasard(int max) {
        return (int) (random() * max);
    }

    void pourcentageCorrect(City ville) {
        if (ville.bonheur >= 100) {
            ville.bonheur = 100;
        }
        if (ville.pollution <= 0) {
            ville.pollution = 0;
        }
    }

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
            Decisions courant = newDecisions(nom, desc, argent, pollution, bonheur, message);
            decisions[idxD - 1] = courant;
        }
        return decisions;
    }

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
            Evenements courant = newEvenements(nom, desc, argent, pollution, bonheur);
            evenements[idxE - 1] = courant;
        }
        return evenements;
    }

    void eventRandom(City ville) {
        Evenements[] listeEvent = loadEvenements(EVENT);
        int nb = tirerAuHasard(100);
        if (nb <= 20 && ville.tour > 1) {
            Evenements event = listeEvent[(int) (random() * length(listeEvent))];
            println(event.desc);
            ville.budget = ville.budget - event.argent;
            ville.pollution = ville.pollution - event.pollution;
            ville.bonheur = ville.bonheur - event.bonheur;
            pourcentageCorrect(ville);
            
            if(event.argent >= 0){
                println("Vous avez gagné " + event.argent + "$ !");
            } else {
                println("Vous avez perdu " + event.argent + "$ !");
            }
            if(event.bonheur >= 0){
                println("Vous avez gagné " + event.bonheur + "% de bonheur !");
            } else {
                println("Vous avez perdu " + event.bonheur + "% de bonheur !");
            }
            if(event.pollution >= 0){
                println("Vous avez gagné " + event.pollution + "% de pollution !");
            } else {
                println("Vous avez perdu " + event.pollution + "% de pollution !");
            }
            delay(3000);
        }
    }

    int botPlay(City ville, Decisions[] decisions) {
        // Situation critique : prioriser le problème le plus urgent
        if (ville.bonheur <= 60) {
            // Chercher la décision qui donne le plus de bonheur
            int meilleurChoix = 1;
            int meilleurBonheur = decisions[0].bonheur;
            
            for (int i = 1; i < 4; i++) {
                if (decisions[i].bonheur > meilleurBonheur) {
                    meilleurChoix = i + 1;
                    meilleurBonheur = decisions[i].bonheur;
                }
            }
            println("Le bot choisit l'option " + meilleurChoix + " pour augmenter le bonheur");
            return meilleurChoix;
        }
        
        if (ville.pollution >= 80) {
            // Chercher la décision qui réduit le plus la pollution
            int meilleurChoix = 1;
            int meilleurePollution = decisions[0].pollution;
            
            for (int i = 1; i < 4; i++) {
                if (decisions[i].pollution < meilleurePollution) {
                    meilleurChoix = i + 1;
                    meilleurePollution = decisions[i].pollution;
                }
            }
            println("Le bot choisit l'option " + meilleurChoix + " pour réduire la pollution");
            return meilleurChoix;
        }
        
        if (ville.budget <= 10000) {
            // Chercher la décision qui rapporte le plus d'argent
            int meilleurChoix = 1;
            int meilleurArgent = decisions[0].argent;
            
            for (int i = 1; i < 4; i++) {
                if (decisions[i].argent > meilleurArgent) {
                    meilleurChoix = i + 1;
                    meilleurArgent = decisions[i].argent;
                }
            }
            println("Le bot choisit l'option " + meilleurChoix + " pour augmenter le budget");
            return meilleurChoix;
        }
        
        // Situation normale : chercher la décision la plus équilibrée
        int meilleurChoix = 1;
        double meilleurScore = evaluerDecision(decisions[0]);
        
        for (int i = 1; i < 4; i++) {
            double score = evaluerDecision(decisions[i]);
            if (score > meilleurScore) {
                meilleurChoix = i + 1;
                meilleurScore = score;
            }
        }
        
        println("Le bot choisit l'option " + meilleurChoix + " pour maintenir l'équilibre");
        return meilleurChoix;
    }

    double evaluerDecision(Decisions decision) {
        // Calcule un score basé sur les impacts de la décision
        // Donne plus de poids aux aspects positifs
        double score = 0;
        score += decision.argent / 1000.0;  // Convertit l'argent en points (1 point par 1000€)
        score -= decision.pollution * 2;     // Pénalise fortement la pollution
        score += decision.bonheur * 1.5;     // Favorise le bonheur
        return score;
    }

    void sauvegarderPartie(City ville) {
        CSVFile existingSaves = loadCSV("ressources/save.csv");
        int existingRows = rowCount(existingSaves);
        
        // Vérifier si la ville existe déjà dans les sauvegardes
        currentSaveIndex = -1;
        for (int i = 0; i < existingRows && currentSaveIndex == -1; i++) {
            if (getCell(existingSaves, i, 0).equals(ville.nom)) {
                currentSaveIndex = i;
            }
        }

        String[][] contenu;
        if (existingRows > 0) {
            contenu = new String[existingRows][5];
            for (int i = 0; i < existingRows; i++) {
                for (int j = 0; j < 5; j++) {
                    contenu[i][j] = getCell(existingSaves, i, j);
                }
            }
        } else {
            contenu = new String[1][5];
        }

        // Si la ville n'existe pas, créer une nouvelle entrée
        if (currentSaveIndex == -1) {
            String[][] nouveauContenu = new String[existingRows + 1][5];
            for (int i = 0; i < existingRows; i++) {
                for (int j = 0; j < 5; j++) {
                    nouveauContenu[i][j] = contenu[i][j];
                }
            }
            currentSaveIndex = existingRows;
            contenu = nouveauContenu;
        }

        // Mettre à jour la sauvegarde
        contenu[currentSaveIndex][0] = ville.nom;
        contenu[currentSaveIndex][1] = "" + ville.tour;
        contenu[currentSaveIndex][2] = "" + ville.budget;
        contenu[currentSaveIndex][3] = "" + ville.pollution;
        contenu[currentSaveIndex][4] = "" + ville.bonheur;

        saveCSV(contenu, "ressources/save.csv");
        println("Partie sauvegardée !");
    }

    void chargerPartie(City ville) {
        CSVFile saveFile = loadCSV("ressources/save.csv");
        int totalSaves = rowCount(saveFile);

        if (totalSaves == 0) {
            println("Aucune sauvegarde trouvée.");
            return;
        }

        println("Parties sauvegardées :");
        for (int i = 0; i < totalSaves; i++) {
            println((i + 1) + ". " + getCell(saveFile, i, 0));
        }

        int choix = choixValideNbr(totalSaves);
        currentSaveIndex = choix - 1;
        ville.nom = getCell(saveFile, currentSaveIndex, 0);
        ville.tour = stringToInt(getCell(saveFile, currentSaveIndex, 1));
        ville.budget = stringToInt(getCell(saveFile, currentSaveIndex, 2));
        ville.pollution = stringToInt(getCell(saveFile, currentSaveIndex, 3));
        ville.bonheur = stringToInt(getCell(saveFile, currentSaveIndex, 4));

        println("Partie chargée !");
    }

    String extractionChamps(String line, int fieldIndex) {
        int currentIndex = 0;
        int start = 0;
        for (int i = 0; i < length(line); i++) {
            if (charAt(line, i) == ',' || i == length(line) - 1) {
                if (currentIndex == fieldIndex) {
                    if (i == length(line) - 1) {
                        return substring(line, start, i + 1);
                    }
                    return substring(line, start, i);
                }
                start = i + 1;
                currentIndex++;
            }
        }
        return ""; // En cas de problème (à priori impossible)
    }

    Decisions[] genererDecisionsAleatoires(Decisions[] listeDecisions) {
        Decisions[] decisions = new Decisions[4];
        for (int i = 0; i < 4; i++) {
            decisions[i] = listeDecisions[(int) (random() * 73)];
        }
        return decisions;
    }

    void afficherDecisions(Decisions[] decisions) {
        for (int i = 0; i < 4; i++) {
            println((i+1) + ". " + decisions[i].desc + " (" + decisions[i].argent + " €, " 
                    + decisions[i].pollution + " pollution, " + decisions[i].bonheur + " bonheur)");
        }
        println("- - - - - - - - - - - - - - - - - - -");
        println("Pour revenir au menu et sauvegarder, entrez 5");
    }

    void supprimerSauvegarde(String nomVille) {
        CSVFile existingSaves = loadCSV("ressources/save.csv");
        int existingRows = rowCount(existingSaves);
        
        // Créer un nouveau tableau sans la ville à supprimer
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
}
