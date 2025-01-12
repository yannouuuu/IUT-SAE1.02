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

    void algorithm() {
        clearScreen();
        Decisions[] listeDecisions = loadDecision(DECISIONS);
        City ville = creerPartie();
        initialiserJeu(ville);
        while (ville.tour < 30 && ville.bonheur >= 50 && ville.pollution <= 100 && ville.budget >= 0) {
            clearScreen();
            afficherTxt(TITRE);
            afficherEtatJeu(ville);
            pourcentageCorrect(ville);
            Decisions num1 = listeDecisions[0];
            Decisions num2 = listeDecisions[1];
            Decisions num3 = listeDecisions[2];
            Decisions num4 = listeDecisions[3];
            println("1. " + num1.desc + " (" + num1.argent + " €, " + num1.pollution + " pollution, " + num1.bonheur + " bonheur)");
            println("2. " + num2.desc + " (" + num2.argent + " €, " + num2.pollution + " pollution, " + num2.bonheur + " bonheur)");
            println("3. " + num3.desc + " (" + num3.argent + " €, " + num3.pollution + " pollution, " + num3.bonheur + " bonheur)");
            println("4. " + num4.desc + " (" + num4.argent + " €, " + num4.pollution + " pollution, " + num4.bonheur + " bonheur)");
            
            int choix = choixValideNbr(4); // Remplacer par botPlay(); pour activer le systeme de bot
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
            // Vérifie si l'entrée est vide
            if (length(input) > 0) {
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

    int botPlay() {
        int choix = tirerAuHasard(4) + 1;
        println("Le bot a choisi l'option : " + choix);
        return choix;
    }

    void sauvegarderPartie(City ville) {
        String[][] contenu = {
            {ville.nom, "" + ville.tour, "" + ville.budget, "" + ville.pollution, "" + ville.bonheur}
        };
        
        // Vérifier si le fichier est prêt, sinon il sera créé par saveCSV
        File saveFile = newFile("ressources/save.csv");
        if (!ready(saveFile)) {
            println("Création du fichier de sauvegarde...");
        }
        
        saveCSV(contenu, "ressources/save.csv");
        println("Partie sauvegardée !");
    }

    void chargerPartie(City ville) {
        File saveFile = newFile("ressources/save.csv");
        if (!ready(saveFile)) {
            println("Aucune sauvegarde trouvée.");
            return;
        }
        println("Parties sauvegardées :");
        int index = 1;
        while (ready(saveFile)) {
            String line = readLine(saveFile);
            println(index + ". " + line);
            index++;
        }

        int choix = choixValideNbr(index - 1);
        saveFile = newFile("ressources/save.csv");
        for (int i = 0; i < choix; i++) {
            String line = readLine(saveFile);
            if (i == choix - 1) {
                ville.nom = extractionChamps(line, 0);
                ville.tour = stringToInt(extractionChamps(line, 1));
                ville.budget = stringToInt(extractionChamps(line, 2));
                ville.pollution = stringToInt(extractionChamps(line, 3));
                ville.bonheur = stringToInt(extractionChamps(line, 4));
            }
        }
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