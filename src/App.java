/**
 * App.java
 * 
 * Point d'entrée principal pour l'application EcoManager.
 *
 * Auteurs : Yann RENARD, Yanis MEKKI
 */

// TODO 
// [] Faire quelques tests
// [] Systeme sauvegarde
// [] Systeme de chargement de sauvegarde
// [x] Systeme de bot

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

    void algorithm() {
        clearScreen();
        Decisions[] listeDecisions = loadDecision(DECISIONS);
        City ville = creerPartie();
        initialiserJeu(ville);
        int choix=-1;
        while (ville.tour < 30 && ville.bonheur >= 50 && ville.pollution <= 100 && ville.budget >= 0) {
            clearScreen();
            pourcentageCorrect(ville);
            afficherTxt(TITRE);
            afficherEtatJeu(ville);
            Decisions num1 = listeDecisions[(int) (random()*73)];
            Decisions num2 = listeDecisions[(int) (random()*73)];
            Decisions num3 = listeDecisions[(int) (random()*73)];
            Decisions num4 = listeDecisions[(int) (random()*73)];
            println("1. " + num1.desc + " (" + num1.argent + " €, " + num1.pollution + " pollution, " + num1.bonheur + " bonheur)");
            println("2. " + num2.desc + " (" + num2.argent + " €, " + num2.pollution + " pollution, " + num2.bonheur + " bonheur)");
            println("3. " + num3.desc + " (" + num3.argent + " €, " + num3.pollution + " pollution, " + num3.bonheur + " bonheur)");
            println("4. " + num4.desc + " (" + num4.argent + " €, " + num4.pollution + " pollution, " + num4.bonheur + " bonheur)");
            println("- - - - - - - - - - - - - - - - - - -");
            println("Pour revenir au menu, appuyez sur 'q'");
            
            choix = choixValideNbr(4); // Remplacer par botPlay(); pour activer le systeme de bot
            if (choix == 1) {
                ville.tour++;
                ville.budget = ville.budget + num1.argent;
                ville.pollution = ville.pollution + num1.pollution;
                ville.bonheur = ville.bonheur + num1.bonheur;
                println(num1.message);
            }
            if (choix == 2) {
                ville.tour++;
                ville.budget = ville.budget + num2.argent;
                ville.pollution = ville.pollution + num2.pollution;
                ville.bonheur = ville.bonheur + num2.bonheur;
                println(num2.message);
            }
            if (choix == 3) {
                ville.tour++;
                ville.budget = ville.budget + num3.argent;
                ville.pollution = ville.pollution + num3.pollution;
                ville.bonheur = ville.bonheur + num3.bonheur;
                println(num3.message);
            }
            if (choix == 4) {
                ville.tour++;
                ville.budget = ville.budget + num4.argent;
                ville.pollution = ville.pollution + num4.pollution;
                ville.bonheur = ville.bonheur + num4.bonheur;
                println(num4.message);
            }
            if (choix == 'q') {
                sauvegarderPartie(ville);
                initialiserJeu(ville);
            }
        }

        if (ville.bonheur < 50 || ville.pollution > 100 || ville.budget < 0) {
            afficherTxt(LOSE); 
            delay(10000);
        } else if (ville.tour >= 30) {
            afficherTxt(WIN);
            delay(10000);
        } else {
            sauvegarderPartie(ville);
        }
    }

    void initialiserJeu(City ville) {
        afficherTxt(TITRE);
        afficherMenuStart();
        int choix = choixValideNbr(4);
        startSelect(choix, ville);
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

    int choixValideNbr(int nbrChoix) {
        println("- - - - - - - - - - - - - - - - -");
        print("Choisissez une action (1-" + nbrChoix + ") : ");
        
        while (true) {
            String input = readString();
            // Vérifie si l'entrée est non vide et contient un seul caractère numérique
            if (length(input) == 1 && charAt(input, 0) >= '1' && charAt(input, 0) <= (char)('0' + nbrChoix)) {
                int choix = (int)charAt(input, 0) - '0';
                return choix;
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
        return (int)(random() * max);
    }
    
    void pourcentageCorrect(City ville) {
        if (ville.bonheur >= 100) {
            ville.bonheur = 100;
        }
        if (ville.pollution <= 0) {
            ville.pollution = 0;
        }
    }

    Decisions newDecisions(String nom, String desc, int argent, int pollution, int bonheur) {
        Decisions decision = new Decisions();
        decision.nom = nom;
        decision.desc = desc;
        decision.argent = argent;
        decision.pollution = pollution;
        decision.bonheur = bonheur;
        return decision;
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

    Evenements[] loadEvenements(String nomFile){
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

    Decisions[] loadDecision(String nomFile) {
        CSVFile deciAsString = loadCSV(nomFile);
        Decisions[] decisions = new Decisions[rowCount(deciAsString) - 1];
        for (int idxD = 1; idxD < length(decisions) + 1; idxD++) {
            String nom = getCell(deciAsString, idxD, 0);
            String desc = getCell(deciAsString, idxD, 1);
            int argent = stringToInt(getCell(deciAsString, idxD, 2));
            int pollution = stringToInt(getCell(deciAsString, idxD, 3));
            int bonheur = stringToInt(getCell(deciAsString, idxD, 4));
            if (rowCount(deciAsString) > idxD) {
                String message = getCell(deciAsString, idxD, 5);
            } else {
                String message = ""; // or handle the case where the message is not available
            }
            Decisions courant = newDecisions(nom, desc, argent, pollution, bonheur, message);
            decisions[idxD - 1] = courant;
        }
        return decisions;
    }

    int botPlay() {
        int choix = tirerAuHasard(4) + 1;
        println("Le bot a choisi l'option : " + choix);
        return choix;
    }

    void sauvegarderPartie(City ville) {
        // Charger les saves existantes
        CSVFile existingSaves = loadCSV("ressources/save.csv");
        int existingRows = rowCount(existingSaves);

        String[][] contenu = new String[existingRows + 1][5];
        
        for (int i = 0; i < existingRows; i++) {
            for (int j = 0; j < 5; j++) {
                contenu[i][j] = getCell(existingSaves, i, j);
            }
        }

        contenu[existingRows - 1][0] = ville.nom;
        contenu[existingRows - 1][1] = "" + ville.tour;
        contenu[existingRows - 1][2] = "" + ville.budget;
        contenu[existingRows - 1][3] = "" + ville.pollution;
        contenu[existingRows - 1][4] = "" + ville.bonheur;
        
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
        ville.nom = getCell(saveFile, choix - 1, 0);
        ville.tour = stringToInt(getCell(saveFile, choix - 1, 1));
        ville.budget = stringToInt(getCell(saveFile, choix - 1, 2));
        ville.pollution = stringToInt(getCell(saveFile, choix - 1, 3));
        ville.bonheur = stringToInt(getCell(saveFile, choix - 1, 4));
        
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
}