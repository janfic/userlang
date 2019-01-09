package com.janfic.isp2019

/**
 *  The transcriber of the Translator. Takes in translations and writes the translation into the current file 
 */
class Writer {
    
    //Tools
    private static File file
    private static PrintStream output
   
    /**
     *   Sets the writer's file. This creates also creates the new output file. *.groovy
     **/
    public static void setFile(File f) {
        file = f
        output = new PrintStream(new File(f.name.replace(".txt",".groovy")))
    }
    
    /**
     *   Takes any translation map and translate the map to the according type of definition
     **/
    public static void write(Map translation) {
        if(translation.type.equals("Component")) {
            writeComponent(translation)
        }
        else if(translation.type.equals("Script")) {
            writeScript(translation)
        }
    }
    
    /**
    *  Writes a component defintion based on the translation given to it
    */
    public static void writeComponent(Map translation) {
        output.println "package pack.${translation.pack}.components" // package writing
        output.println ""
        output.println "import pack.${translation.pack}.scripts.*" // import in pack items
        output.println "import pack.${translation.pack}.assets.*"
        output.println ""
        output.println "import com.badlogic.ashley.core.Component" // import Component 
        output.println ""
        output.println "class $translation.name implements Component {" // start of class
        translation.fields.each({output.println "\t$it.value $it.key"}) // var decs
        output.println ""
        output.println "\t$translation.name() {" // constructor
        translation.defaults.each({ //setting vars to default
                if(it.value instanceof Map) { // if a script/asset
                    output.println "\t\t$it.key = new $it.value.script(${it.value - [script:it.value.script]})()".replace("[","").replace("]","") // write initializtion of script and run it
                }
                else {
                    output.println "\t\t$it.key = $it.value"
                }
            })
        output.println "\t}"
        
        output.print "}" // end class
    }
    
    /**
    *   Writes an Entity Definition based on the translation given to it
    */
    public static void writeEntity(Map translation) {
        
    }
    
    /**
    *   Writes a System Definition based on the translation given to it
    */
    public static void writeSystem(Map translation) {
        
    }
    
    /**
    *   Writes a Pack Definition based on the translation given to it
    */
    public static void writePack(Map translation) {
        
    }
    
    /**
    *   Writes a Asset Definition based on the translation given to it
    */
    public static void writeAsset(Map translation) {
        
    }
    
    /**
    *   Writes a Script Definition based on the translation given to it
    */
    public static void writeScript(Map translation) {
        output.println "package pack.${translation.pack}.scripts" // writes package
        output.println ""
        output.println "class $translation.name extends Closure {" // start of class
        translation.given.each({output.println "\t$it.value $it.key"}) // writes each var dec, the "given"
        output.println ""
        output.println "\t@Override" //writes the "run" 
        output.println "\tdef call() {"
        output.println "\t\t${translation.body}"
        output.println "\t}"
        output.println ""
        output.println "\t${translation.name}() {" // initializer
        output.println "\t\tsuper(null)"
        output.println "\t}"
        output.println "}"
    }
}