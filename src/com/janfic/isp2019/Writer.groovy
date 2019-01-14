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
        output = new PrintStream(new File(f.name.substring(0, f.name.lastIndexOf(".")) + ".groovy"))
    }
    
    /**
     *   Takes any translation map and translate the map to the according type of definition
     **/
    public static void write(Map translation) {
        if(translation.type.equals("Component")) {
            writeComponent(translation)
        }
        else if(translation.type.equals("Asset")) {
            writeAsset(translation)
        }
        else if(translation.type.equals("Entity")) {
            writeEntity(translation)
        }
        else if(translation.type.equals("System")) {
            writeSystem(translation)
        }
    }
    
    /**
     *  Writes a component defintion based on the translation given to it
     */
    public static void writeComponent(Map translation) {
        output.println "package pack.${translation.pack}.components" // package writing
        output.println ""
        output.println "import pack.${translation.pack}.assets.*"
        output.println ""
        output.println "import com.badlogic.ashley.core.*" // imports 
        output.println "import com.badlogic.gdx.graphics.g2d.*"  
        output.println "import com.badlogic.gdx.files.*"
        output.println "import com.badlogic.gdx.math.*"
        output.println "import com.badlogic.gdx.graphics.*" 
        output.println ""
        output.println "class $translation.name implements Component {" // start of class
        writeFields(translation.fields)
        output.println ""
        output.println "\t$translation.name() {" // constructor
        writeDefaults(translation.defaults)
        output.println "\t}"
        output.print "}" // end class
    }
    
    /**
     *   Writes an Entity Definition based on the translation given to it
     */
    public static void writeEntity(Map translation) {
        output.println "package pack.${translation.pack}.entities"
        output.println ""
        output.println "import pack.${translation.pack}.assets.*"
        output.println "import pack.${translation.pack}.components.*"
        output.println ""
        output.println "import com.badlogic.ashley.core.Entity"
        output.println ""
        output.println "class $translation.name extends Closre<Entity> {"
        output.println "\t@Override"
        output.println "\tEntity call() {"
        output.println "\t\tEntity entity = new Entity()"
        writeComponents(translation.components, translation.defaults)
        output.println "\t\treturn entity"
        output.println "\t}"
        output.println ""
        output.println "\t$translation.name() {"
        output.println "\t\tsuper(null)"
        output.println "\t}"
        output.println "}"
    }
    
    public static void writeComponents(String[] comps, Map defaults) {
        comps.each({
                output.print "\t\tentity.add(new $it("
                writeMap(defaults[it.toString()])
                output.println "))"
            })
    }
        
    
    /**
     *   Writes a System Definition based on the translation given to it
     */
    public static void writeSystem(Map translation) {
        output.println "package pack.${translation.pack}.systems"
        output.println ""
        output.println "import pack.${translation.pack}.components"
        output.println "import pack.${translation.pack}.assets"
        output.println ""
        output.println "import com.badlogic.ashley.core.*"
        output.println ""
        output.println "class $translation.name extends EntitySytem {"
        output.println "\tprivate ImmutableArray<Entity> entities"
        output.println ""
        translation.family.each({output.println "\tprivate ComponentMapper<$it.value> ${it.value.toString().toLowerCase()}Mapper = ComponentMapper.getFor(${it.value}.class)"})
        output.println ""
        output.println "\tvoid addedToEngine(Engine engine) {"
        output.println "\t\tentities = engine.getFor("
        output.println "\t\t\tFamily.all("
        (translation.family as Map).eachWithIndex({ key, value, index ->
                output.print "\t\t\t\t${value}.class"
                if(index < translation.family.size() - 1) {
                    output.println ","
                }
                else {
                    output.println ""
                }
            })
        output.println "\t\t\t).get()"
        output.println "\t\t)"
        output.println "\t}"
        output.println ""
        output.println "\tvoid update(float deltaTime) {"
        output.println "\t\tfor(entity in entities) {"
        (translation.family as Map).each({output.println "\t\t\t${it.value} $it.key = ${it.value.toString().toLowerCase()}Mapper.get(entity)"})
        output.println "" 
        output.println "\t\t\t${translation.body.replaceAll("\\n\\t*","\n\t\t\t")}"
        output.println "\t\t}"
        output.println "\t}"
        output.println "}"
    }
    
    /**
     *   Writes a Pack Definition based on the translation given to it
     */
    public static void writePack(Map translation) {
        
    }
    
    /**
     *   Writes a Script Definition based on the translation given to it
     */
    public static void writeAsset(Map translation) {
        output.println "package pack.${translation.pack}.assets" // writes package
        output.println ""
        output.println "import com.badlogic.ashley.core.*" // imports 
        output.println "import com.badlogic.gdx.graphics.g2d.*"  
        output.println "import com.badlogic.gdx.files.*"
        output.println "import com.badlogic.gdx.math.*"
        output.println "import com.badlogic.gdx.graphics.*" 
        output.println ""
        output.println "class $translation.name extends Closure {" // start of class
        writeFields(translation.given)
        output.println ""
        output.println "\t@Override" //writes the "run" 
        output.println "\tdef call() {"
        output.println "\t\t${translation.body}"
        output.println "\t}"
        output.println ""
        output.println "\t${translation.name}() {" // initializer
        output.println "\t\tsuper(null)"
        writeDefaults(translation.defaults)
        output.println "\t}"
        output.println "}"
    }
    
    /**
     *  Writes field/given declarations 
     */
    public static void writeFields(Map fields) {
        fields.each({output.println "\t${it.value.getSimpleName()} $it.key"})
    }
    
    /**
     *   Writes initializsers in the constructor. AKA the defaults of the definition
     */
    public static void writeDefaults(Map defaults) {
        defaults.each({
                output.print "\t\t$it.key = "
                if(it.value instanceof Map) {
                    writeAssetCall(it.value)
                    output.println()
                }
                else {
                    if(it.value instanceof String)
                    output.println "\"$it.value\""
                    else
                    output.println "$it.value"
                }
            })
    }
    
    /**
     *   Writes a Script or Asset call
     */ 
    public static void writeAssetCall(Map call) {
        output.print "new "
        if(call.asset) {
            output.print "$call.asset("
            if(call.size() > 1){
                call = call - [asset:call.asset]
                writeMap(call)
            }
            output.print ")()"
        }
        else {
            println "NOT A VALID SCRIPT/ASSET CALL"
        }
    }
    
    public static void writeMap(Map map) {
        int index = 0
        for( e in map) {
            if(e.key.equals("asset")) {
                writeAssetCall(map)
                break
            }
            else if(e.value instanceof Map) {
                output.print "$e.key:"
                writeMap(e.value)
            }
            else if(e.value instanceof String ){
                output.print "$e.key:\"$e.value\""
            }
            else {
                output.print "$e.key:${e.value}"
            }
            output.print "${index < map.size() - 1 ? " , " : ""}"
            index++
        }
    }
}