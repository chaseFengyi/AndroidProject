package com.analysistools.filecontrol;

public class SortModel {
	 private String name;   //��ʾ������  
	    private String sortLetters;  //�ؼ���  
	      
	    public String getName() {  
	        return name;  
	    }  
	    public void setName(String name) {  
	        this.name = name;  
	    }  
	    public String getSortLetters() {  
	        return sortLetters;  
	    }  
	    public void setSortLetters(String sortLetters) {  
	        this.sortLetters = sortLetters;  
	    }
		@Override
		public String toString() {
			return "SortModel [name=" + name + ", sortLetters=" + sortLetters
					+ "]";
		}  
	    
}
