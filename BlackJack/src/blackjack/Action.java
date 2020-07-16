package blackjack;

public enum Action {
	HIT {
		@Override public String toString(){ 
			return "Hit"; 
		} 
	}, 
	
	STAND { 
		@Override public String toString(){ 
			return "Stand"; 
		} 
	}, 
	
	SPLIT { 
		@Override public String toString(){ 
			return "Split"; 
		} 
	},
	
	DOUBLE {
		@Override public String toString(){ 
			return "Double"; 
		} 
	}, 
	
	SURRENDER { 
		@Override public String toString(){ 
			return "Surrender"; 
		} 
	};

}