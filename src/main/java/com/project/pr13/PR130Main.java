package com.project.pr13;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.project.pr13.format.PersonaFormatter;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Classe principal que gestiona la lectura i el processament de fitxers XML per obtenir dades de persones.
 * 
 * Aquesta classe s'encarrega de llegir un fitxer XML que conté informació de persones,
 * processar-lo i mostrar les dades formatades per consola.
 */
public class PR130Main {

    private final File dataDir;

    /**
     * Constructor de la classe PR130Main.
     * 
     * @param dataDir Directori on es troben els fitxers de dades.
     */
    public PR130Main(File dataDir) {
        this.dataDir = dataDir;
    }

    /**
     * Mètode principal que inicia l'execució del programa.
     * 
     * @param args Arguments passats a la línia de comandament (no s'utilitzen en aquest programa).
     */
    public static void main(String[] args) {
        String userDir = System.getProperty("user.dir");
        File dataDir = new File(userDir, "data" + File.separator + "pr13");

        PR130Main app = new PR130Main(dataDir);
        app.processarFitxerXML("persones.xml");
    }

    /**
     * Processa un fitxer XML per obtenir la informació de les persones i imprimir-la.
     * 
     * @param filename Nom del fitxer XML a processar.
     */
    public void processarFitxerXML(String filename) {
        File inputFile = new File(dataDir, filename);
        Document doc = parseXML(inputFile);
        if (doc != null) {
            NodeList persones = doc.getElementsByTagName("persona");
            // imprimirCapçaleres();
            // imprimirDadesPersones(persones);
        }
    }

    /**
     * Llegeix un fitxer XML i el converteix en un objecte Document.
     * 
     * @param inputFile Fitxer XML a llegir.
     * @return Document XML carregat o null si hi ha hagut un error en la lectura.
     */
    public static Document parseXML(File inputFile) {
        // *************** CODI PRÀCTICA **********************/
        try {
            if (inputFile.equals(null)){
                throw new RuntimeException();
            }
            DocumentBuilderFactory dbFac = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFac.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);

            doc.getDocumentElement().normalize();

            return doc;


        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new RuntimeException(e);
        }catch (RuntimeException e){
            return null;
        }


    }
}
