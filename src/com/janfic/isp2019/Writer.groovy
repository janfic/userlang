package com.janfic.isp2019

class Writer {
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
    }
    
    public static void writeComponent(Map translation) {
        output.println "package pack.${translation.pack}.components"
        output.println ""
        output.println "import pack.${translation.pack}.scripts.*"
        output.println "import pack.${translation.pack}.assets.*"
        output.println ""
        output.println "import com.badlogic.ashley.core.Component"
        output.println ""
        output.println "class $translation.name implements Component {"
        translation.fields.each({output.println "\t$it.value $it.key"})
        output.println ""
        output.println "\t$translation.name() {"
        translation.defaults.each({
                if(it.value instanceof Map) {
                    output.println "\t\t$it.key = new $it.value.script(${it.value - [script:it.value.script]})()"
                }
                else {
                    output.println "\t\t$it.key = $it.value"
                }
            })
        output.println "\t}"
        
        output.print "}"
    }
    public static void writeEntity(Map translation) {
        
    }
    public static void writeSystem(Map translation) {
        
    }
    public static void writePack(Map translation) {
        
    }
    public static void writeAsset(Map translation) {
        
    }
    public static void writeScript(Map translation) { //function
        
    }
}