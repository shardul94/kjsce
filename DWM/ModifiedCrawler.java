import java.util.*;
import java.net.*;
class ModifiedCrawler{
	public static void main(String args[]) throws Exception{
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter the keyword to search: ");
		String keyword = sc.nextLine();
		URL startUrl = new URL("http://www.somaiya.edu/VidyaVihar/kjsce/");
		Vector<URL> urls = new Vector<URL>(); //TO store all urls
		Vector<URL> key_urls = new Vector<URL>(); //To store urls in which keyword is found
		Vector<Integer> key_count = new Vector<Integer>(); //To store freq in each matched url
		urls.add(startUrl); //Add intial URL
		System.out.println("\nSearched Pages:");
		for(int i=0;i<urls.size();i++){
			URL u = urls.get(i);
			Scanner in = new Scanner(u.openStream()); //Get the URL stream into Scanner
			System.out.println("("+(i+1)+")"+u.toString()); //Print the current url
			boolean found = false; //Check if keyword has been found in the current page
			int count=0; //To store freq of keyword in current page
			while(in.hasNextLine()){
				String s = in.nextLine();
				int start=0,end=0;
				while((start=s.indexOf(keyword,start))!=-1){//If line contains keyword multiple times, we need while loop
					if(found!=true){//If it has already been found in the page previously, dont add the page to key_url again
						found = true;
						key_urls.add(u);
					}
					count++;
					start++;
				}
				start=0;end=0;
				while((start=s.indexOf("<a href='http://www.somaiya.edu/",start))!=-1){//Find links in the page
					end = s.indexOf("'>",start);
					String new_url = s.substring(start+9,end);//start+9 cz we want to skip first 9 chars which are <a href='
					if(!urls.contains(new URL(new_url))){//Check if the found link is not already present in url list
						urls.add(new URL(new_url));
					}
					start = end; //Search for new link from end of first
				}
			}
			if(found) key_count.add(count);
		}
		System.out.println();
		if(key_urls.size()==0) System.out.println("Keyword not found");
		else{
			System.out.println("Keyword found at:");
			int i=0,maxfrequrl=0;
			for(URL u:key_urls){//Printing of matched URLs and calculation of max frequency URL
				if(key_count.get(i)>maxfrequrl)
					maxfrequrl=i;
				System.out.println("("+(i+1)+")"+u.toString()+" Frequency:"+key_count.get(i));
				i++;
			}
			System.out.println("\nMost Relevant URL:"); //Printing of max frequency URL
			System.out.println(key_urls.get(maxfrequrl).toString()+" Frequency:"+key_count.get(maxfrequrl));
		}
	}
}