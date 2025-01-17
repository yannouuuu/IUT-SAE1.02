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
// [] Fix la saisie 5 qui renvoie 'la saisie est invalide' 

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

            int choix = choixValideNbr(5); // Remplacer par botPlay(); pour activer le systeme de bot
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
            delay(10000);
            jeu();
        } else if (ville.tour >= 30) {
            afficherTxt(WIN);
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
        } else if (choix == 2) {
            ville.tour++;
            ville.budget = ville.budget + num2.argent;
            ville.pollution = ville.pollution + num2.pollution;
            ville.bonheur = ville.bonheur + num2.bonheur;
            println(num2.message);
        } else if (choix == 3) {
            ville.tour++;
            ville.budget = ville.budget + num3.argent;
            ville.pollution = ville.pollution + num3.pollution;
            ville.bonheur = ville.bonheur + num3.bonheur;
            println(num3.message);
        } else if (choix == 4) {
            ville.tour++;
            ville.budget = ville.budget + num4.argent;
            ville.pollution = ville.pollution + num4.pollution;
            ville.bonheur = ville.bonheur + num4.bonheur;
            println(num4.message);
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
            delay(3000); // Réduit à 3 secondes au lieu de 30
        }
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

        String[][] contenu;

        if (existingRows > 0) {
            // Si des sauvegardes existent, on les charge dans un tableau
            contenu = new String[existingRows][5];
            for (int i = 0; i < existingRows; i++) {
                for (int j = 0; j < 5; j++) {
                    contenu[i][j] = getCell(existingSaves, i, j);
                }
            }
        } else {
            // Sinon, on crée un nouveau tableau
            contenu = new String[1][5];
        }

        // On écrase la sauvegarde sélectionnée
        int saveIndex = choixValideNbr(existingRows); // On demande à l'utilisateur de choisir une sauvegarde à écraser
        contenu[saveIndex - 1][0] = ville.nom;
        contenu[saveIndex - 1][1] = "" + ville.tour;
        contenu[saveIndex - 1][2] = "" + ville.budget;
        contenu[saveIndex - 1][3] = "" + ville.pollution;
        contenu[saveIndex - 1][4] = "" + ville.bonheur;

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
}
