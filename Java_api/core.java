/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package xmi_api;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
/**
 *
 * @author UX31
 */
public class core {
    private static  Document Doc = null; 
    
    public static Document parser(File xmifile){  
        try{
         DocumentBuilderFactory dbFactory 
            = DocumentBuilderFactory.newInstance();
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         Doc = dBuilder.parse(xmifile);
         
        }catch(Exception e){
        System.out.println("Error in parsing");
        }
        return Doc;
    }
    
    // get RootNode
    public static Element getRootNode(){
     return Doc.getDocumentElement();
    }
    // get array of immediate children
    public static Node[] getChildren(Node node){
       NodeList node_list = node.getChildNodes();
       Node[] node_array = new Node[node_list.getLength()];
       
        for (int i=0; i< node_array.length;i++){
           node_array[i] = node_list.item(i);
        }
        return node_array; 
    }
    
    // get all nodes 
    public static Node[] getAllNodes(){
        NodeList nList = Doc.getElementsByTagName("*");
        Node[] nodes = new Node[nList.getLength()]; 
        
        for(int i=0;i<nodes.length;i++){
            nodes[i]=nList.item(i);
        }
        return nodes;
    }
    
    // get all meta nodes
    public static Node[] getAllMetaNodes(){
    Node[] nodes = getAllNodes(); 
    Node[] metaNodes = new Node[getNumOfMetaNodes()];
    int j = 0;
    for (int i=1;i<nodes.length;i++){
        if (isMetaNode(nodes[i])){
            metaNodes[j] = nodes[i];
            j++;
        }
    }
    return metaNodes;
    }
    
    // get number of meta nodes
    public static int getNumOfMetaNodes(){
    Node[] nodes = getAllNodes();
    int j = 0;
    for (int i=1; i< nodes.length; i++){  // neglect the root node             
        if(isMetaNode(nodes[i])){
        j++;
        }
    }
    return j;
    }
    
    // get attribute of node with specific attribute name
    public static String getAttribute(Node node, String name){
       String Attribute = null;
       NamedNodeMap nodemap = node.getAttributes();
       if (nodemap.getNamedItem(name)!= null){
           Attribute = nodemap.getNamedItem(name).getNodeValue();
       }
     return  Attribute;
    }
    
    // get array of attribute name of a node
    public static String[] getAttributeNames(Node node){
       NamedNodeMap nodemap = node.getAttributes();
       String[] AttributeNames = new String[nodemap.getLength()];
       for (int i=0; i< nodemap.getLength();i++){
           AttributeNames[i] = nodemap.item(i).getNodeName();         
       }        
        return AttributeNames;
    }
    
    // get relative id of node     
    public static String getRelid(Node node){        
        return getAttribute(node,"relid");
    }
    
    // get guid of node 
    public static String getGuid(Node node){
       return getAttribute(node, "id");
    }
    
    // check whether node is meta node    
    public static boolean isMetaNode(Node node){       
        String metanode = getAttribute(node,"isMeta");
        String fstring = "false";
        boolean m = false;
        if (metanode.equals(fstring)){
            m = false;
        } 
        else  m = true;      
        return m;
    }
    
    // get node by guid 
    public static Node getNodeByGuid(String guid){
    Node[] allNodes = getAllNodes();
    Node node = null;
    for (int i=1; i<allNodes.length;i++){
     if(getGuid(allNodes[i]).equals(guid)){
         node = allNodes[i];
     }
    }
    return node;
    }
    // get base of node
    public static Node getBase(Node node){
        String baseID = getAttribute(node,"base");
        Node baseNode = null;
        if(baseID != null){
            baseNode = getNodeByGuid(baseID);
        }
        return baseNode;
    }
    
    // get pointer names 
    public static String[] getPointerNames(Node node){
    String[] AttributeNames = getAttributeNames(node); 
    String[] pointerNames = new String[getNumOfPointerNames(node)];    
    int j=0; 
    for(int i=0;i<AttributeNames.length;i++){
        if ((AttributeNames[i].contains("-dst-"))){
            pointerNames[j] = "dst";
            j++;
        }
        else if ((AttributeNames[i].contains("-src-"))){
            pointerNames[j] = "src";
            j++;
        }
            ;        
    }   
    return pointerNames;            
    }
    
    // get number of pointer names 
    private static int getNumOfPointerNames(Node node){
    String[] AttributeNames = getAttributeNames(node); 
    int num = 0; 
        for(int i=0;i<AttributeNames.length;i++){
        if ((AttributeNames[i].contains("-dst-"))||(AttributeNames[i].contains("-src-"))){
            num++;
        }
            ;        
    }
    return num;
    }
    
    // get pointer - return a target node of a given pointer 
    public static Node getPointer(Node node, String pointerName){
    String[] AttributeNames = getAttributeNames(node);
    String   pointerValue = null;
    Node targetNode = null;
    if (AttributeNames !=null){
        for (int i=0; i<AttributeNames.length;i++){
            if ((AttributeNames[i].contains(pointerName))){
                pointerValue = getAttribute(node,AttributeNames[i]);                
                targetNode = getNodeByGuid(pointerValue);
            }   
        }   
    }
    
    return targetNode; 
    }
    
    // get collection names - return list of pointer names that has the node as target
    
    public static String[] getCollectionNames(Node node){
    String[] AttributeNames = getAttributeNames(node);
    String[] CollectionNames = new String[getNumOfCollectionNames(node)];
    int k = 0;
        for (int i=0; i<AttributeNames.length; i++){
        if(AttributeNames[i].contains("invrel")){
           CollectionNames[k] = AttributeNames[i];
           k++;
        }
    }
    return CollectionNames;
    }
    
    // get number of collection names of a node 
    private static int getNumOfCollectionNames(Node node){
    int num=0;
    String[] AttributeNames = getAttributeNames(node);   
    for (int i=0; i<AttributeNames.length; i++){
        if(AttributeNames[i].contains("invrel")){
            num++;
        }
    }
    return num;
    }
    
    // get collection - return a list of reverse pointer (invrel) - are these nodes?
    public static Node[] getCollection(Node node, String pointerName){
    String[] AttributeNames = getAttributeNames(node);
    int num = getNumOfCollectionNames(node); 
    Node[] Collections = new Node[num];
    if(num>0){
        int j=0;
        for(int i=0; i<AttributeNames.length; i++){
            if(AttributeNames[i].contains(pointerName)){
                String collectionID = getAttribute(node,AttributeNames[i]);
                Collections[j]= getNodeByGuid(collectionID);
                j++;
            }
    
        }
    }
    
    return Collections;
    }
    

    // get Path of a node 
    public static String getPath(Node node){
    Node[] Ancestors = getAllAncestors(node);    
    String path = "/";   
    if(Ancestors.length > 2){
        for(int i = Ancestors.length-2; i>=0; i--){
        if(getRelid(Ancestors[i])!=null){            
            path += getRelid(Ancestors[i]);
            path +="/";
        }

    }
    }
    path +=getRelid(node);
    return path;
    }
    
    // get number of Ancestors of a node
    public static int getNumOfAncestor(Node node){
    int i = 0;
    Node parentNode = node.getParentNode();
    if (parentNode != null){ 
        while (parentNode != null){
            parentNode = parentNode.getParentNode();
            i++;
    
        }
    } 
    return i;
    }
    
    // get all ancestors of a node
    public static Node[] getAllAncestors(Node node){
    Node[] Ancestors = new Node[getNumOfAncestor(node)];
    Node parentNode = node.getParentNode();
    int i = 0;
    if (parentNode != null){ 
        while (parentNode != null){
            Ancestors[i] = parentNode;
            parentNode = parentNode.getParentNode();
            i++;
    
        }
    } 
    return Ancestors;
    }
    
    // get Node by Path 
    public static Node getNodeByPath(String path){
    Node node = getRootNode(); 
    String[] Relids = getRelidsFromPath(path);
    boolean has = false;
    
    int i = 0; 
    while(i < Relids.length){
        Node[] children = getChildren(node);
        int j=0; 
        while(j<children.length){
            if (getRelid(children[j]).equals(Relids[i])){
                node = children[j];
                has = true;
                break;
            }
            j++;        
        }
        if(has){
        i++;
        }
        else{
        node = null;
        break;
        }        
        
    }
    
    return node;
    }
    
    // get relative id array from path 
    
    private static String[] getRelidsFromPath(String path){
    
     String Relids = path.replaceAll("/", "");
    String[] RelidArray = new String[Relids.length()];  
    for (int i=0;i<Relids.length();i++){
        RelidArray[i] = Character.toString(Relids.charAt(i));   
        
    }
    return RelidArray;
    }
    
    // get metatype of a node - return meta-type node 
    
    public static Node getMetaType(Node node){
    Node metaNode = node;   
    while(true){
        if (isMetaNode(metaNode)){            
            break;
        }
        else{
            metaNode = getBase(metaNode);
            if(metaNode == null){
                break;
            }
        }
        
    }   
    return metaNode; 
    }
    
    // get children metatype - return list of immediate children of meta type    
    public static Node[] getChildrenMetaType(Node node){        
    Node[] metaChildren = new Node[getNumOfChildrenMetaType(node)];
    if(isMetaNode(node)){
        Node[] allNodes = getAllNodes(); 
        int j=0;
        for (int i=0;i<allNodes.length;i++){
            if (allNodes[i]!= node){
                if(getBase(allNodes[i])== node){
                    metaChildren[j] = allNodes[i];
                    j++;
                }
            }    
        }
    }
        
    return metaChildren; 
    }
    
    // get number of children of metatype of a node
    private static int getNumOfChildrenMetaType(Node node){
    int num=0; 
    if(isMetaNode(node)){
        Node[] allNodes = getAllNodes(); 
        for (int i=0;i<allNodes.length;i++){
            if (allNodes[i]!= node){
                if(getBase(allNodes[i])== node){
                    num++;
                }
            }    
        }
    }
    return num;
    }
}   
