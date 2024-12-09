/**
 * App.java
 * 
 * Point d'entrée principal pour l'application EcoManager.
 * Contient la méthode `main` qui initialise et démarre le jeu.
 *
 * Auteur(s) : Yann RENARD, Yanis MEKKI
 */

import extensions.File;
import extensions.CSVFile;

class App extends Program {

    final String NOM = "EcoManager";
    final String TITRE = "../ressources/titre_ascii.txt";
    final String RULES = "../ressources/rules.txt";
    final String DECISIONS="../ressources/decisions.csv";
    final String WIN="../ressources/win.txt";
    final String LOOSE="../ressources/win.txt";



    void algorithm(){
        Decisions[] liste_decisions=loadcsv(DECISIONS);
        City ville= creerpartie();
        start(ville);
        while(ville.tour<30 && ville.bonheur>=50 && ville.pollution<=100 && ville.budget>=0){
            afficherEtatJeu(ville);
            Decisions num1=liste_decisions[0];
            Decisions num2=liste_decisions[1];
            Decisions num3=liste_decisions[2];
            Decisions num4=liste_decisions[3];
            println("1. "+num1.desc+" ("+num1.argent+" €, "+num1.pollution+" pollution, "+num1.bonheur+" bonheur)");
            println("2. "+num2.desc+" ("+num2.argent+" €, "+num2.pollution+" pollution, "+num2.bonheur+" bonheur)");
            println("3. "+num3.desc+" ("+num3.argent+" €, "+num3.pollution+" pollution, "+num3.bonheur+" bonheur)");
            println("4. "+num4.desc+" ("+num4.argent+" €, "+num4.pollution+" pollution, "+num4.bonheur+" bonheur)");
            println("Choisissez une action (1-4)");
            int choix=choixvalidenbr(4);
            if (choix==1){
                ville.tour++;
                ville.budget=ville.budget+num1.argent;
                ville.pollution=ville.pollution+num1.pollution;
                ville.bonheur=ville.bonheur+num1.bonheur;
                println(num1.message);
            }
            if (choix==2){
                ville.tour++;
                ville.budget=ville.budget+num2.argent;
                ville.pollution=ville.pollution+num2.pollution;
                ville.bonheur=ville.bonheur+num2.bonheur;
                println(num2.message);
            }
            if (choix==3){
                ville.tour++;
                ville.budget=ville.budget+num3.argent;
                ville.pollution=ville.pollution+num3.pollution;
                ville.bonheur=ville.bonheur+num3.bonheur;
                println(num3.message);
            }
            if (choix==4){
                ville.tour++;
                ville.budget=ville.budget+num4.argent;
                ville.pollution=ville.pollution+num4.pollution;
                ville.bonheur=ville.bonheur+num4.bonheur;
                println(num4.message);

            }


             }
        if (ville.bonheur<50 || ville.pollution>100 || ville.budget<0){
            afficherTxT(LOOSE);
        }
        else if(ville.tour>=30){
            afficherTxT(WIN);
        }
        }
        
        

        
        
        // Boucle principale du jeu
        // while (budget >= /* valeur */ || pollution >= /* valeur */ || bonheur >= /* valeur */) {
        //     afficherEtatJeu(tour, budget, pollution, bonheur);
        //     tour++;
        // }
    
    void afficherMenuStart(){
        println("Choisissez une option :");
        println("1. Nouvelle ville");
        println("2. Charger une partie");
        println("3. Afficher les règles du jeu");
        println("Ctrl + C. pour quitter le jeu");

    }
    void StartSelect(int choix, City ville){
        if(choix == 1){
            println("Quel est le nom de votre ville ?");
            String nom = readString();
            ville.nom = nom;
        }
        else if(choix == 2){
            println("Systeme de chargement de sauvegarde pas encore disponible mais ca va lancer une partie au nom default");
        }
        else if(choix == 3){
            afficherTxT(RULES);
            choixdeq();
            start(ville);
        } 
          
        
    }

    int choixvalidenbr(int nbrchoix){
        print("Entrez votre choix (1-"+nbrchoix+") : ");
        int choix=readInt();
        while(choix<1 || choix>nbrchoix){
            println("La saisie est invalide veuillez recommencez !");
            choix=readInt();
        }
        return choix;
    }

    char choixdeq(){
        println("pour quitter la page de règles du jeu, appuyez sur 'q')");
        char choix_q=readChar();
        while(choix_q!='q'){
            println("La saisie est invalide veuillez recommencez !");
            choix_q=readChar();
        }
        return choix_q;
    }
    

    // Affichage de l'état du jeu
    void afficherEtatJeu(City ville) {
        println(ville.nom);
        println("---------------------------------");
        println("Tour : " + ville.tour);
        println("Budget : " + ville.budget + " €");
        println("Pollution : " + ville.pollution + "%");
        println("Bonheur : " + ville.bonheur + "%");
    }

    void afficherTxT(String dessin){
        File file = newFile(dessin);
        while(ready(file)){
            println(readLine(file));
        }
    }  
    
    
    City creerpartie(){
        City ville= new City();
        ville.nom="default";
        ville.tour=1;
        ville.budget=45000;
        ville.pollution=20;
        ville.bonheur=85;
        return ville;
    }
    Decisions newDecisions(String nom, String desc, int argent, int pollution, int bonheur, String message){
        Decisions decision= new Decisions();
        decision.nom= nom;
        decision.desc= desc;
        decision.argent= argent;
        decision.pollution= pollution;
        decision.bonheur= bonheur;
        decision.message= message;
        return decision;



    }
    int tirerAuHasard(int max){
		return (int) (random()*max) ;
    }
    void start(City ville){
        afficherTxT(TITRE); // Affiche le titre du jeu
        afficherMenuStart(); // Affiche le menu principal
        int choix = choixvalidenbr(3);
        StartSelect(choix, ville);
    }
    Decisions[] loadcsv(String nom_file) {
        CSVFile deciAsString = loadCSV(nom_file);
        Decisions[] decisions= new Decisions[rowCount(deciAsString)-1];
        for (int idxD=1; idxD<length(decisions)+1; idxD++){
            String nom = getCell(deciAsString,idxD,0);
            String desc = getCell(deciAsString,idxD,1);
            int argent = stringToInt(getCell(deciAsString,idxD,2));
            int pollution = stringToInt(getCell(deciAsString,idxD,3));
            int bonheur = stringToInt(getCell(deciAsString,idxD,4));
            String message = getCell(deciAsString,idxD,5);
            Decisions courant = newDecisions(nom, desc, argent, pollution, bonheur, message);
            decisions[idxD-1] = courant;
        }
        return decisions;
    }
        

        
    
}

