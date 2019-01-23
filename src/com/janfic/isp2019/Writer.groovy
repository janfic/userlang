package com.janfic.isp2019

/**
 *  The transcriber of the Translator. Takes in translations and writes the translation into the current file 
 */
class Writer {
       
    //Tools
    private static File file
    private static PrintStream output
    private static GroovyShell shell
   
    /**
     *   Sets the writer's file. This creates also creates the new output file. *.groovy
     **/
    public static void setFile(File f) {
        file = f
        
        File newFile = new File(f.getParentFile(), "groovy/" + f.getName().replace(".usr",".groovy"))
        newFile.getParentFile().mkdirs()
        newFile.createNewFile()
        output = new PrintStream(newFile)
        shell = new GroovyShell()
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
        else if(translation.type.equals("Pack")) {
            writePack(translation)
        }
    }
    
    /**
     *  Writes a component defintion based on the translation given to it
     */
    public static void writeComponent(Map translation) {
        output.println "package pack.${translation.pack}.components" // set the current package
        output.println ""
        output.println "import pack.${translation.pack}.assets.*" //import assets in current pack
        output.println ""
        output.println "import com.badlogic.ashley.core.*" // imports for commpn libgdx classes
        output.println "import com.badlogic.gdx.graphics.g2d.*"  
        output.println "import com.badlogic.gdx.files.*"
        output.println "import com.badlogic.gdx.math.*"
        output.println "import com.badlogic.gdx.graphics.*" 
        output.println ""
        output.println "class $translation.name implements Component {" // start of class
        writeFields(translation.fields) //write fields
        output.println ""
        output.println "\t$translation.name() {" // constructor
        writeDefaults(translation.defaults) // write initializers
        output.println "\t}"
        output.print "}" // end class
    }
    
    /**
     *   Writes an Entity Definition based on the translation given to it
     */
    public static void writeEntity(Map translation) {
        output.println "package pack.${translation.pack}.entities" // write current pack
        output.println ""
        output.println "import pack.${translation.pack}.assets.*" // import assets and components in current pack
        output.println "import pack.${translation.pack}.components.*"
        output.println ""
        writeUses(translation.uses) // write imports for dependencies of this entity
        output.println ""
        output.println "import com.badlogic.ashley.core.*" // imports for common libgdx tools
        output.println "import com.badlogic.gdx.graphics.g2d.*"  
        output.println "import com.badlogic.gdx.files.*"
        output.println "import com.badlogic.gdx.math.*"
        output.println "import com.badlogic.gdx.graphics.*" 
        output.println ""
        output.println "class $translation.name extends Closure<Entity> {" //class start
        output.println "\t@Override"
        output.println "\tEntity call() {"
        output.println "\t\tEntity entity = new Entity()" // make new empty entity
        writeComponents(translation.components, translation.defaults) // write add components to new entity
        output.println "\t\treturn entity" // return new entity
        output.println "\t}"
        output.println ""
        output.println "\t$translation.name() {" //constructor for this closure
        output.println "\t\tsuper(null)"
        output.println "\t}"
        output.println "}"
    }
    
    /**
     *  Writes the list of components to add to entity during initialization
     */
    public static void writeComponents(String[] comps, Map defaults = [:]) {
        comps.each({
                output.print "\t\tentity.add(new $it(" // make new component
                if(defaults) { // add defaults if they are there
                    Map d = defaults[it.toString()] 
                    if(d != null) {
                        writeMap(d)
                    }
                }
                output.println "))"
            })
    }
    
    /**
     *   Writes a System Definition based on the translation given to it
     */
    public static void writeSystem(Map translation) {
        output.println "package pack.${translation.pack}.systems" // writes pack
        output.println ""
        output.println "import pack.${translation.pack}.components.*" //imports
        output.println "import pack.${translation.pack}.assets.*" 
        output.println "import pack.${translation.pack}.entities.*"
        output.println ""
        writeUses(translation.uses as String[]) //imports dependencies
        output.println ""
        output.println "import com.badlogic.ashley.core.*" //import libgdx tools
        output.println "import com.badlogic.gdx.graphics.g2d.*"  
        output.println "import com.badlogic.gdx.files.*"
        output.println "import com.badlogic.gdx.math.*"
        output.println "import com.badlogic.gdx.graphics.*" 
        output.println "import com.badlogic.gdx.graphics.glutil.*" 
        output.println ""
        output.println "class $translation.name extends EntitySystem {" // start class
        for(family in (translation.families as Map)) { // writes each family as a field
            output.println "\tprivate ImmutableArray<Entity> $family.key"
        }
        output.println ""
        // makes one list of unique components 
        List components = []
        translation.families.each({
                components += it.value.collect({key, value -> value})
            })
        components.unique({a , b -> a <=> b})
        components.each({output.println "\tprivate ComponentMapper<$it> ${it.toLowerCase()}Mapper = ComponentMapper.getFor(${it}.class)"}) // makes component apper fields
        //
        output.println ""
        writeFields(translation.fields) // writes fields of the system
        output.println ""
        output.println "\t$translation.name() {" // constructor
        writeDefaults(translation.defaults) // defaults writen for fields
        output.println "\t}"
        output.println ""
        output.println "\tvoid addedToEngine(Engine engine) {" // when the system is added to engine it must make the families/array of entities
        for(family in (translation.families as Map)) { // makes each family
            output.println "\t\t$family.key = engine.getFor("
            output.println "\t\t\tFamily.all("
            (family.value as Map).eachWithIndex({ key, value, index -> //iterates through each component of each family
                    output.print "\t\t\t\t${value}.class"
                    if(index < family.value.size() - 1) {
                        output.println ","
                    }
                    else {
                        output.println ""
                    }
                })
            output.println "\t\t\t).get()"
            output.println "\t\t)"
        }
        output.println "\t}"
        output.println ""
        output.println "\tvoid update(float deltaTime) {" //per frame update method
        if(translation.eachFrame) // if the translation has a frame body
            writeBody("\t\t$translation.eachFrame")
        for(family in translation.eachEntity) { // writes each body of eachEntity family:{}
            output.println "\t\tfor( entity in $family.key) {"
            for(c in translation.families."$family.key") {
                output.println "\t\t\t$c.value $c.key = ${c.value.toLowerCase()}Mapper.get(entity)"
            }
            writeBody("\t\t\t$family.value")
            output.println "\t\t}"
        }
        if(translation.endFrame) // if the translation has an end frame body
            writeBody("\t\t$translation.endFrame")
        output.println "\t}"
        output.println "}"
    }
    
    /**  
     *  Writes a Body of a System or Asset. Replaces all asset calls with its translation 
     */
    public static void writeBody(String body) {
        output.print "\t\t\t"
        def assetCalls = body.findAll("\\[\\s*(asset|entity)\\s*:\\s*\"[a-zA-Z]+\".*\\]")
        def parts = body.split("\\[\\s*(asset|entity)\\s*:\\s*\"[a-zA-Z]+\".*\\]")
        if(assetCalls.size() > 0) {
            println "here"
            parts.eachWithIndex({ str, int index ->
                    output.print str
                    if(index<parts.size() - 1)
                    writeAssetCall((Map)shell.evaluate(assetCalls.get(index)))
                })
        }
        else 
        output.print body
        output.println ""
        //output.println "\t\t\t${body.replaceAll("\\n\\t*","\n\t\t\t")}"
    }
    
    /**
     *   Writes a Pack Definition based on the translation given to it
     */
    public static void writePack(Map translation) {
        output.println "package pack.${translation.pack}"
        output.println ""
        output.println "class $translation.name extends Pack {"
        output.println "\t$translation.name() {"
        output.println "\t\tsuper("
        output.println "\t\t\tname:\"$translation.name\","
        output.println "\t\t\tdisplay:\"$translation.display\","
        output.println "\t\t\tinfo:\"$translation.info\","
        output.print "\t\t\tdependencies:["
        translation.uses.eachWithIndex({ it , index ->
                output.print "new pack.$it.$it()"
                if(index < translation.uses.size() - 2) {
                    output.print ","
                }
                output.print ""
            })
        output.println "],"
        output.print "\t\t\tassets:["
        if(translation.assets)
        output.print "\"${translation.assets.join("\",\"")}\""
        output.println "],"
        output.print "\t\t\tcomponents:["
        if(translation.components)
        output.print "\"${translation.components.join("\",\"")}\""
        output.println "],"
        output.print "\t\t\tentities:["
        if(translation.entities)
        output.print "\"${translation.entities.join("\",\"")}\""
        output.println "],"
        output.print "\t\t\tsystems:["
        if(translation.systems)
        output.print "\"${translation.systems.join("\",\"")}\""
        output.println "],"
        if(translation.active)
        output.print "\"${translation.active.join("\",\"")}\""
        output.println "]"
        output.println "\t\t)"
        output.println "\t}"
        output.println "}"
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
        writeBody(translation.body)
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
    public static String writeAssetCall(Map call) {
        output.print "new "
        if(call.asset) {
            output.print "$call.asset("
            if(call.size() > 1){
                call = call - [asset:call.asset]
                writeMap(call)
            }
            output.print ")()"
        }
        else if(call.entity) {
            output.print "$call.entity("
            if(call.size() > 1){
                call = call - [entity:call.entity]
                writeMap(call)
            }
            output.print ")()"
        }
        else if(call.make) {
            output.print "${call.make.getSimpleName()}("
            if(call.size() > 1){
                call = call - [make:call.make]
                writeMap(call)
            }
            output.print ")"
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
    
    public static void writeUses(String[] uses) {
        uses.each({
                output.println "import pack.${it}.*"
                output.println "import pack.${it}.assets.*"
                output.println "import pack.${it}.components.*"
                output.println "import pack.${it}.entities.*"
            })
    }
}