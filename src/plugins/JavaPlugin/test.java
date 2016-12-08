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
public class test {
    
     public static void main(String[] args) {
         
        String filename = args[0];
        core core = new core(); 
        File inputFile = new File(filename);
        
         try {
            core.parser(inputFile);
            
            // test get root node
            System.out.println("****************************************************************");
            System.out.println("****************************************************************");
            System.out.println("********************** TEST GET ROOT NODE *********************");
            System.out.println("Root node name :" + core.getRootNode().getNodeName());
            
            // test get children nodes
            Node[] childNode = core.getChildren(core.getRootNode());
                          
            // test get guid, get attribute, get AttributeNames, get Relative ID, check if MetaNode
            
            System.out.println("****************************************************************");
            System.out.println("********************** TEST GET CHILDREN, GET ATTRIBUTE, GET ATTRIBUTENAMES, GET RELATIVE ID, CHECK IF METANODE *********************");
            System.out.println("****************************************************************");
            for(int i=0; i<childNode.length; i++){           
            System.out.println("Child Node Name :" + childNode[i].getNodeName());
            System.out.println("------------ ID :" + core.getGuid(childNode[i]));
            String[] AttributeNames = new String[core.getAttributeNames(childNode[i]).length];
                     AttributeNames = core.getAttributeNames(childNode[i]);
                    for(int j=0; j<AttributeNames.length;j++){
                      System.out.println("------------ attribute name " + j + " " + AttributeNames[j]);
                    }
                    
            System.out.println("------------ Relative ID : " + core.getRelid(childNode[i]));  
            System.out.println("------------ is a Meta Node? : " + core.isMetaNode(childNode[i]));         
                             
            }
            
            // test get all Nodes 
            System.out.println("****************************************************************");
            System.out.println("****************************************************************");
            System.out.println("********************** TEST GET ALL NODES *********************");
            Node[] allNodes = core.getAllNodes();                        
            System.out.println("Number of Node : " + allNodes.length); 
            for (int i=0; i<allNodes.length;i++){
            System.out.println("------------ Node Name : " + allNodes[i].getNodeName()); 
            }
            
            // test get all MetaNodes
            System.out.println("****************************************************************");
            System.out.println("********************** TEST GET ALL META NODES *********************");
            System.out.println("****************************************************************");
            Node[] MetaNodes = core.getAllMetaNodes();
            System.out.println("Number of Meta Nodes : " + core.getNumOfMetaNodes()); 
            for (int i=0; i<MetaNodes.length;i++){
            System.out.println("------------ Meta Node Name : " + MetaNodes[i].getNodeName()); 
            }
            
            // test get node by guid 
            
            System.out.println("****************************************************************");
            System.out.println("********************** TEST GET NODE BY GUID *********************");
            System.out.println("****************************************************************");
            for (int i=1;i<allNodes.length;i++){
            System.out.println("Node with guid " + core.getGuid(allNodes[i]) + " is " + core.getNodeByGuid(core.getGuid(allNodes[i])).getNodeName()); 
            }
            
            // test get base node 
            
            System.out.println("****************************************************************");
            System.out.println("********************** TEST GET BASE NODE *********************");
            System.out.println("****************************************************************");
            for (int i=2;i<allNodes.length;i++){
                if(core.getBase(allNodes[i]) != null){
                    System.out.println("Base node of " + allNodes[i].getNodeName() + " is " + core.getBase(allNodes[i]).getNodeName()); 
                }
            }
            
            // test get pointer names 
            System.out.println("****************************************************************");
            System.out.println("********************** TEST GET POINTER NAMES *********************");
            System.out.println("****************************************************************");
            for(int i=0; i<allNodes.length;i++){
                String[] pointerNames = core.getPointerNames(allNodes[i]);
                if(pointerNames !=null){
                for (int j=0;j<pointerNames.length;j++){
                    System.out.println("Pointer Name "+ (j+1) + " of node " + allNodes[i].getNodeName() + " is " + pointerNames[j]);
                }
            }
            }
            
            // test get pointer - return target Node of given pointer 
            
            System.out.println("****************************************************************");
            System.out.println("********************** TEST GET POINTER *********************");
            System.out.println("****************************************************************");
            for(int i=0; i<allNodes.length;i++){
                String[] pointerNames = core.getPointerNames(allNodes[i]);
                if(pointerNames !=null){
                for (int j=0;j<pointerNames.length;j++){
                    if (core.getPointer(allNodes[i],pointerNames[j])!= null){
                        System.out.println("Target Node of the pointer Name "+ pointerNames[j] + " is " + core.getPointer(allNodes[i],pointerNames[j]).getNodeName());
                    }
                    
                }
            }
            }
            
            // test get path : return path of a node
            
            System.out.println("****************************************************************");
            System.out.println("********************** TEST GET PATH *********************");         
            System.out.println("****************************************************************");
            for(int j=0;j<allNodes.length;j++){
                Node[] Ancestors = core.getAllAncestors(allNodes[j]);
                if (allNodes[j].getNodeName()!= null){
                    System.out.println("Number of Ancestors of Node " +allNodes[j].getNodeName() + " is: " +Ancestors.length);
                    System.out.println("Ancestors name: ");
                }
                if (Ancestors.length > 0){
                    for (int i=0; i<Ancestors.length;i++){
                        if (Ancestors[i].getNodeName()!= null){
                            System.out.println("" +Ancestors[i].getNodeName());

                        }
                    }
                }
                System.out.println("Path of node " + allNodes[j].getNodeName()+ " is: " +core.getPath(allNodes[j]));
               
            } 
           
            // test get Node by path 
            
            System.out.println("****************************************************************");
            System.out.println("********************** TEST GET NODE BY PATH *********************"); 
            System.out.println("****************************************************************");
            for (int i=1; i<allNodes.length; i++){
                String path = core.getPath(allNodes[i]);                     
                System.out.println("Node with path "+path+ " is " + core.getNodeByPath(path).getNodeName());
            
            }
            
            // test get metatype : return meta-type node
            
            System.out.println("****************************************************************");
            System.out.println("********************** TEST GET METATYPE OF A NODE *********************"); 
            System.out.println("****************************************************************");
            for (int i=1; i<allNodes.length; i++){                                  
                System.out.println("MetaType of node "+allNodes[i].getNodeName()+ " with ID: " +core.getGuid(allNodes[i])+ " is node " + core.getMetaType(allNodes[i]).getNodeName()+ " with ID: "+core.getGuid(core.getMetaType(allNodes[i])));           
            }
             
            // test get children meta type - return list of immediate children of metaType
            
            System.out.println("****************************************************************");
            System.out.println("********************** TEST GET CHILDREN OF METATYPE *********************"); 
            System.out.println("****************************************************************");
            for (int i=1; i<allNodes.length; i++){
                if(core.isMetaNode(allNodes[i])){
                    Node[] metaChildren = core.getChildrenMetaType(allNodes[i]);
                    System.out.println("Immediate children of metatype "+allNodes[i].getNodeName()+ " with ID: " +core.getGuid(allNodes[i])+ " are: "); 
                    if(metaChildren !=null){
                        for(int j=0; j<metaChildren.length; j++){
                           System.out.println(""+metaChildren[j].getNodeName()+" with ID " +core.getGuid(metaChildren[j])); 
                        }
                    
                    }
                    System.out.println("-----------------------------------------------------------------------------------------"); 
                }
                            
            }
            
            
            // test get collection names - return list of pointer names that has node as target
            
            System.out.println("****************************************************************");
            System.out.println("********************** TEST GET COLLECTION NAMES *********************");
            System.out.println("****************************************************************");
            for(int i = 1; i < allNodes.length; i++){
                String[] CollectionNames = core.getCollectionNames(allNodes[i]); 
               
                if (CollectionNames.length == 0){
                    System.out.println("Node "+allNodes[i].getNodeName()+ " with ID: " +core.getGuid(allNodes[i])+ " don't have any collection name");
                }
                else{
                    System.out.println("Collection Names of node "+allNodes[i].getNodeName()+ " with ID: " +core.getGuid(allNodes[i])+ " are: "); 
                    for (int j=0; j< CollectionNames.length; j++){
                        System.out.println(""+CollectionNames[j]);
                    }
                }
                System.out.println("-----------------------------------------------------------------------------------------"); 
            }
            
            
            // test get collection - return list of reverse pointer (are these node?)            
            
            System.out.println("****************************************************************");
            System.out.println("********************** TEST GET COLLECTION *********************");
            System.out.println("****************************************************************");
            for(int i=1; i<allNodes.length;i++){
                Node[] collections = core.getCollection(allNodes[i],"invrel");
                if (collections.length>0){
                    System.out.println("Collections of node "+allNodes[i].getNodeName()+ " with ID: " +core.getGuid(allNodes[i])+ " are:");
                    for(int j=0; j<collections.length; j++){
                        System.out.println("Node "+collections[j].getNodeName()+" with ID " +core.getGuid(collections[j]));
                    }
                    System.out.println("-----------------------------------------------------------------------------------------"); 
            }
                
            }
            
            
                      
        }catch (Exception e) {
            System.err.println("Exception occured while loading the resource file for configuration model: " + e.getMessage()); 
        }
              
    }
        
}
