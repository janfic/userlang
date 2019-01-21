package pack.agar

class Agar extends Pack {
	Agar() {
		super(
			name:"Agar",
			display:"CORE",
			info:"A pack creating a clone of agar.io",
			dependencies:[new pack.Core.Core()],
			assets:[],
			components:["MassComponent","NameComponent","CircleComponent","PopComponent"],
			entities:[],
			systems:["MassSystem","CollisionSystem"]
		)
	}
}
