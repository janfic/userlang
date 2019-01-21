package com.janfic.useride

class Pack {
	
	String name, display, info, author
	
	Pack[] dependencies
	String[] assets, components, entities, systems
	
	boolean compiled = false
	
	final void compile() {
		for( dependency in dependencies) {
			if(!dependency.isCompiled()) {
				dependency.compile()
			}
		}
		for(asset in assets) {
			
		}
		for(component in components) {
			
		}
		for(entity in entities) {
			
		}
		for(system in systems) {
			
		}
		compiled = true
	}
	
	boolean isCompiled() {
		return compiled
	}
}