package com.project.pr13;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.project.pr13.format.AsciiTablePrinter;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Classe principal que permet gestionar un fitxer XML de cursos amb opcions per llistar, afegir i eliminar alumnes, 
 * així com mostrar informació dels cursos i mòduls.
 * 
 * Aquesta classe inclou funcionalitats per interactuar amb un fitxer XML, executar operacions de consulta,
 * i realitzar modificacions en el contingut del fitxer.
 */
public class PR132Main {

    private final Path xmlFilePath;
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Constructor de la classe PR132Main.
     * 
     * @param xmlFilePath Ruta al fitxer XML que conté la informació dels cursos.
     */
    public PR132Main(Path xmlFilePath) {
        this.xmlFilePath = xmlFilePath;
    }

    /**
     * Mètode principal que inicia l'execució del programa.
     * 
     * @param args Arguments passats a la línia de comandament (no s'utilitzen en aquest programa).
     */
    public static void main(String[] args) {
        String userDir = System.getProperty("user.dir");
        Path xmlFilePath = Paths.get(userDir, "data", "pr13", "cursos.xml");

        PR132Main app = new PR132Main(xmlFilePath);
        app.executar();
    }

    /**
     * Executa el menú principal del programa fins que l'usuari decideixi sortir.
     */
    public void executar() {
        boolean exit = false;
        while (!exit) {
            mostrarMenu();
            System.out.print("Escull una opció: ");
            int opcio = scanner.nextInt();
            scanner.nextLine(); // Netegem el buffer del scanner
            exit = processarOpcio(opcio);
        }
    }

    /**
     * Processa l'opció seleccionada per l'usuari.
     * 
     * @param opcio Opció seleccionada al menú.
     * @return True si l'usuari decideix sortir del programa, false en cas contrari.
     */
    public boolean processarOpcio(int opcio) {
        String cursId;
        String nomAlumne;
        switch (opcio) {
            case 1:
                List<List<String>> cursos = llistarCursos();
                imprimirTaulaCursos(cursos);
                return false;
            case 2:
                System.out.print("Introdueix l'ID del curs per veure els seus mòduls: ");
                cursId = scanner.nextLine();
                List<List<String>> moduls = mostrarModuls(cursId);
                imprimirTaulaModuls(moduls);
                return false;
            case 3:
                System.out.print("Introdueix l'ID del curs per veure la llista d'alumnes: ");
                cursId = scanner.nextLine();
                List<String> alumnes = llistarAlumnes(cursId);
                imprimirLlistaAlumnes(alumnes);
                return false;
            case 4:
                System.out.print("Introdueix l'ID del curs on vols afegir l'alumne: ");
                cursId = scanner.nextLine();
                System.out.print("Introdueix el nom complet de l'alumne a afegir: ");
                nomAlumne = scanner.nextLine();
                afegirAlumne(cursId, nomAlumne);
                return false;
            case 5:
                System.out.print("Introdueix l'ID del curs on vols eliminar l'alumne: ");
                cursId = scanner.nextLine();
                System.out.print("Introdueix el nom complet de l'alumne a eliminar: ");
                nomAlumne = scanner.nextLine();
                eliminarAlumne(cursId, nomAlumne);
                return false;
            case 6:
                System.out.println("Sortint del programa...");
                return true;
            default:
                System.out.println("Opció no reconeguda. Si us plau, prova de nou.");
                return false;
        }
    }

    /**
     * Mostra el menú principal amb les opcions disponibles.
     */
    private void mostrarMenu() {
        System.out.println("\nMENÚ PRINCIPAL");
        System.out.println("1. Llistar IDs de cursos i tutors");
        System.out.println("2. Mostrar IDs i títols dels mòduls d'un curs");
        System.out.println("3. Llistar alumnes d’un curs");
        System.out.println("4. Afegir un alumne a un curs");
        System.out.println("5. Eliminar un alumne d'un curs");
        System.out.println("6. Sortir");
    }

    /**
     * Llegeix el fitxer XML i llista tots els cursos amb el seu tutor i nombre d'alumnes.
     * 
     * @return Llista amb la informació dels cursos (ID, tutor, nombre d'alumnes).
     */
    public List<List<String>> llistarCursos() {
        // *************** CODI PRÀCTICA **********************/
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(String.valueOf(xmlFilePath)));
            List<String> listAlumnosFin = new ArrayList<>();
            List<String> listIdFin = new ArrayList<>();
            List<String> listProfesFin = new ArrayList<>();
            List<List<String>> listFin = new ArrayList<>();
            XPath xpath = XPathFactory.newInstance().newXPath();
            String exp = "/cursos/curs";
            NodeList listId = (NodeList) xpath.compile(exp).evaluate(doc,XPathConstants.NODESET);

            for (int i = 0; i<listId.getLength();i++){
                System.out.println(i);
                String id = "";

                Node nodeId = listId.item(i);
                //System.out.println("el id es: "+nodeId);

                Element elm = (Element) listId.item(i);
                id = elm.getAttribute("id");
                //System.out.println("el id es:"+id);
                listIdFin.add(id);

                //System.out.println(id);
                //System.out.println("/cursos/curs[@id = \'"+id+"\']/tutor");
                String exprProf = "/cursos/curs[@id = '"+id+"']/tutor";
                NodeList listProfes = (NodeList) xpath.compile(exprProf).evaluate(doc,XPathConstants.NODESET);
                Node nodo = listProfes.item(i);
                if (nodo != null){
                    listProfesFin.add(nodo.getTextContent().trim());
                }


                String expAlumn = "count(/cursos/curs[@id = \'"+id+"\']/alumnes/alumne)";
                Double liscontal = (Double) xpath.compile(expAlumn).evaluate(doc,XPathConstants.NUMBER);
                listAlumnosFin.add(""+liscontal.intValue());
                //System.out.println(liscontal.intValue());





            }
            listFin.add(0,listIdFin);
            listFin.add(1,listProfesFin);
            listFin.add(2,listAlumnosFin);
            return listFin;
        } catch (ParserConfigurationException | IOException | SAXException e) {
            throw new RuntimeException(e);
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Imprimeix per consola una taula amb la informació dels cursos.
     * 
     * @param cursos Llista amb la informació dels cursos.
     */
    public void imprimirTaulaCursos(List<List<String>> cursos) {
        List<String> capçaleres = List.of("ID", "Tutor", "Total Alumnes");
        AsciiTablePrinter.imprimirTaula(capçaleres, cursos);
    }

    /**
     * Mostra els mòduls d'un curs especificat pel seu ID.
     * 
     * @param idCurs ID del curs del qual es volen veure els mòduls.
     * @return Llista amb la informació dels mòduls (ID, títol).
     */
    public List<List<String>> mostrarModuls(String idCurs) {
        // *************** CODI PRÀCTICA **********************/
        List<List<String>> modulsInfo = new ArrayList<>();

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(String.valueOf(xmlFilePath)));
            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xpath = xPathFactory.newXPath();
            String expression = "/cursos/curs[@id='" + idCurs + "']/moduls/modul";
            XPathExpression xPathexp = xpath.compile(expression);


            NodeList nodeList = (NodeList) xPathexp.evaluate(doc, XPathConstants.NODESET);

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node modulNode = nodeList.item(i);

                String modulId = modulNode.getAttributes().getNamedItem("id").getNodeValue();
                String modulTitol = modulNode.getChildNodes().item(0).getTextContent(); // Asumiendo que el primer hijo es el título

                List<String> modulData = new ArrayList<>();
                modulData.add(modulId);
                modulData.add(modulTitol.trim());

                modulsInfo.add(modulData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return modulsInfo; // Substitueix pel teu
    }

    /**
     * Imprimeix per consola una taula amb la informació dels mòduls.
     * 
     * @param moduls Llista amb la informació dels mòduls.
     */
    public void imprimirTaulaModuls(List<List<String>> moduls) {
        List<String> capçaleres = List.of("ID Mòdul", "Títol");
        AsciiTablePrinter.imprimirTaula(capçaleres, moduls);
    }

    /**
     * Llista els alumnes inscrits en un curs especificat pel seu ID.
     * 
     * @param idCurs ID del curs del qual es volen veure els alumnes.
     * @return Llista amb els noms dels alumnes.
     */
    public List<String> llistarAlumnes(String idCurs) {
        // *************** CODI PRÀCTICA **********************/
        List<String> alumnes = new ArrayList<>();
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(String.valueOf(xmlFilePath)));
            XPathFactory xPathFact = XPathFactory.newInstance();
            XPath xpath = xPathFact.newXPath();
            String exp = "/cursos/curs[@id='"+idCurs+"']/alumnes/alumne";
            XPathExpression xPathexp = xpath.compile(exp);
            NodeList nodeList = (NodeList) xPathexp.evaluate(doc, XPathConstants.NODESET);
            for (int i = 0; i < nodeList.getLength(); i++) {
                alumnes.add(nodeList.item(i).getTextContent().trim());
                //System.out.println(nodeList.item(i).getTextContent().trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return alumnes; // Substitueix pel teu
    }

    /**
     * Imprimeix per consola la llista d'alumnes d'un curs.
     * 
     * @param alumnes Llista d'alumnes a imprimir.
     */
    public void imprimirLlistaAlumnes(List<String> alumnes) {
        System.out.println("Alumnes:");
        alumnes.forEach(alumne -> System.out.println("- " + alumne));
    }

    /**
     * Afegeix un alumne a un curs especificat pel seu ID.
     * 
     * @param idCurs ID del curs on es vol afegir l'alumne.
     * @param nomAlumne Nom de l'alumne a afegir.
     */
    public void afegirAlumne(String idCurs, String nomAlumne)  {
        // *************** CODI PRÀCTICA **********************/
    try{
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new File(String.valueOf(xmlFilePath)));
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xpath = xPathFactory.newXPath();
        String expression = "/cursos/curs[@id='" + idCurs + "']/alumnes";
        XPathExpression xPathExpression = xpath.compile(expression);
        Node alumnos = (Node) xPathExpression.evaluate(doc, XPathConstants.NODE);

        if (alumnos != null) {
            Element alumneElement = doc.createElement("alumne");
            alumneElement.setTextContent(nomAlumne);
            alumnos.appendChild(alumneElement);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer tr = transformerFactory.newTransformer();
            tr.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource sr = new DOMSource(doc);
            StreamResult res = new StreamResult(String.valueOf(xmlFilePath));
            tr.transform(sr, res);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    }

    /**
     * Elimina un alumne d'un curs especificat pel seu ID.
     * 
     * @param idCurs ID del curs d'on es vol eliminar l'alumne.
     * @param nomAlumne Nom de l'alumne a eliminar.
     */
    public void eliminarAlumne(String idCurs, String nomAlumne) {
        // *************** CODI PRÀCTICA **********************/
    try{
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new File(String.valueOf(xmlFilePath)));
        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xpath = xPathFactory.newXPath();
        String expression = "/cursos/curs[@id='" + idCurs + "']/alumnes/alumne[text()='" + nomAlumne + "']";
        XPathExpression xPathExp = xpath.compile(expression);

        Node alumnos = (Node) xPathExp.evaluate(doc, XPathConstants.NODE);

        if (alumnos != null) {

            Node nodo = alumnos.getParentNode();
            nodo.removeChild(alumnos);
            
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer tr = transformerFactory.newTransformer();
            tr.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource sr = new DOMSource(doc);
            StreamResult res = new StreamResult(String.valueOf(xmlFilePath));
            tr.transform(sr, res);

            System.out.println("Alumne eliminat: " + nomAlumne);
        } else {
            System.out.println("Alumne no trobat: " + nomAlumne + " en el curs ID: " + idCurs);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    }

    /**
     * Carrega el document XML des de la ruta especificada.
     * 
     * @param pathToXml Ruta del fitxer XML a carregar.
     * @return Document XML carregat.
     */
    private Document carregarDocumentXML(Path pathToXml) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(pathToXml.toFile());
        } catch (Exception e) {
            throw new RuntimeException("Error en carregar el document XML.", e);
        }
    }

    /**
     * Guarda el document XML proporcionat en la ruta del fitxer original.
     * 
     * @param document Document XML a guardar.
     */
    private void guardarDocumentXML(Document document) {
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(xmlFilePath.toFile());
            transformer.transform(source, result);
            System.out.println("El fitxer XML ha estat guardat amb èxit.");
        } catch (TransformerException e) {
            System.out.println("Error en guardar el fitxer XML.");
            e.printStackTrace();
        }
    }
}
