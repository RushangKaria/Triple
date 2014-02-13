/*
I have commented the code so that you will find it easy to understand.
Basically it is a very inefficient brute force randomized algorithm

I start off by generating all 2 pairs minus a 1-factor for the the graph K2n
Then I randomly select any three people, and form a triple

Before concluding that they are a triple I check if that pair has appeared before. if not then only are they included.

Since the randomization may generate a lower no of triples then required I continue to iterate till I get the desired no. {since once i get the desired
no I am sure that all pairs are covered exactly once}

This algorithm can be made very efficient and even generalized for k>=3.

*/
import java.io.*;
import java.util.*;

class Pair	//A pair data structure
{
int x,y;	//men and women id
boolean used;	//to mark a pair as used so that we never use it again

	Pair()
	{
	x=-1;	
	y=-1;	
	used=false;
	}

	boolean equals(Pair pair)	///This function is not used
	{
		if(x==pair.x && y==pair.y)
		return true;

	return false;
	}

	String display()
	{
		return"("+this.x+","+this.y+")";
	}
}

class Triples	//a triple data structure
{
	Pair pairs[];

	Triples()
	{
	pairs=new Pair[3];	//Each triple has three pairs
		
		for(int i=0;i<3;i++)
		pairs[i]=new Pair();	//invoke object creation so that a null pointer exception never occurs
	}
}

class DataOfTriple
{
	static InputStreamReader r;
	static BufferedReader input;

	Random rx=new Random();	//our random no generator...can return -ve values also

	int n;		//User input on the no of couples
	int a[];	// an array used to hold the indexes...this is not required but I felt it aids in understanding
	Pair p[];	//This array will hold all the 2-pairs of the complete graph minus 1 factor
	int no_of_triples;

	Triples t[]=new Triples[1000];
	int triple_size=0;

		DataOfTriple(int a)throws IOException
		{
		r=new InputStreamReader(System.in);
		input=new BufferedReader(r);

		n=a;	//since there were problems in array execution I decided to create a new object each time..so I pass n each time
		}

	boolean driver()throws IOException
	{	

	return formTriples(n);	//Even though this is not required I follow the convention of creating a driver method
	}

	boolean formTriples(int n)//This is the function with the core logic
	{
		a=new int[n];
		for(int i=0;i<n;i++)	//assign n indexes
		a[i]=i;

		int n2=2*n;

		int size=(n2*(n2-1))/2-n;	//compute the total no of 2-pairs
		no_of_triples=size/3;		//compute the no of triples

			p=new Pair[size];	

		for(int i=0;i<size;i++)	//again do this so that objects are allocated memory and will not cause a null pointer exception
		p[i]=new Pair();

		formDoubles();	//since p is global array we need not pass it to form all 2 pairs. Actually all variables are global. Poor Coder ;)



	for(int i=0;i<no_of_triples;i++)
	t[i]=new Triples();


		int found=0;

		int tries=0;

			while(tries++<10000000)	//the inefficient loop
			{
				int i=-1,j=-1,k=-1;

				do
				{
				i=Math.abs(rx.nextInt()%size);
				j=Math.abs(rx.nextInt()%size);
				k=Math.abs(rx.nextInt()%size);
				}
				while(i==j || i==k || k==j); //i,j,k must be different else we might try to form pairs of form x,x,x where x is a pair

					if(notPaired(p[i],p[j],p[k],i,j,k) && onlyThree(p[i],p[j],p[k]))	//checking conditions
					{					
					t[triple_size].pairs[0]=p[i];	//the triple and its pairs have not occured before
					t[triple_size].pairs[1]=p[j];	//store it in the triple array
					t[triple_size++].pairs[2]=p[k];	
					p[i].used=true;			//show that the pairs have been used			
					p[j].used=true;			
					p[k].used=true;

					found++;
					}

			if(found==no_of_triples)	//okay so a little bit of optimization.. if i found the desired no before 1000000 iterations
			break;				//i can safely exit...my simulations showed it took roughly 4*no of pairs tries.
			}

		if(found!=no_of_triples)
		return false;
		
		for(int i=0;i<triple_size;i++)	//print the triples if they are of the desired pair
		{
			print(t[i]);
		}

	return true;

			

	}

	boolean onlyThree(Pair A,Pair B,Pair C)	//this checks if the triple is of the form ABC (A,B) (B,C) (A,C) ... |A|=|B|=|C|=2..always
	{
		int counts[]=new int[2*n];
	
		counts[A.x]++;		//increment the counter for a person each time that person appears
		counts[A.y]++;
		counts[B.x]++;
		counts[B.y]++;
		counts[C.x]++;
		counts[C.y]++;

	int counter=0;

		for(int i=0;i<2*n;i++)
		if(counts[i]==2)	//any person must occur only twice
		counter++;

	if(counter==3)	//if 3 persons appear twice then only 3 people were selected to form the triple..since it is random weird pairs might be formed
	return true;	//if you really want to understand the importance of this method try removing it and observe the output for n>=4
	else
	return false;
			
	}

		void print(Triples t)
		{
		System.out.println(t.pairs[0].display()+","+t.pairs[1].display()+","+t.pairs[2].display());
		}

	boolean equal(Pair A,Pair B)
	{
		if(A.x==B.x && A.y == B.y)
		{
		System.out.println(""+A.x+"=="+B.x);
		return true;
		}
		else
		return false;


	}

		boolean notPaired(Pair A,Pair B,Pair C,int x,int y,int z) //Again a crucial method
		{
		int count=0;
		if(p[x].used || p[y].used || p[z].used || A.x==-1 || B.x==-1 || C.x==-1) //if any pair was used, return false
		return false;

		//	System.out.println("Checking for "+A.display()+" "+B.display()+" "+C.display());
			

			for(int i=0;i<triple_size;i++)// This loop simply checks again if any 2 pairs have been paired together more than once
			{			      // This is not required for our homework but it enables vectoring of the pairs.
						      // so if I plan to generalize this program to become a BIBD generator it will be useful
				for(int j=0;j<3;j++)
				{

					if(t[i].pairs[j].x==A.x && t[i].pairs[j].y==A.y)
					{
					count++;
					}
				
				}

				for(int j=0;j<3;j++)
				{
					if(t[i].pairs[j].x==B.x && t[i].pairs[j].y==B.y)
					{
					count++;
					}
				
				}

				for(int j=0;j<3;j++)
				{
					if(t[i].pairs[j].x==C.x && t[i].pairs[j].y==C.y)
					{
					count++;
					}
				}

					if(count>=2)
					{
					return false;
					
					}
			count=0;
			}

		return true;
		}


	void formDoubles()//this is to generate all 2 pairs
	{

	int pair=0;
		

			for(int i=0;i<(2*n)-1;i++)	//till the second last element
			{
			int j=i+1;		//Pairs are always of the form (x,y) x<y due to this	
				while(j<(2*n))
				{
					if(j!=i+n)	//this is the spouse avoiding condition. if you remove the if loop then all 2 pairs are formed
					{		// ofcourse dont forget to make the array as large
					p[pair].x=i;
					p[pair++].y=j;
					}
				j++;
				}
			}
		
	}
}

class Triple
{

	
	static BufferedReader input=new BufferedReader(new InputStreamReader(System.in));;

	public static void main(String args[])throws IOException
	{
	int n;
	boolean stop=false;

		System.out.println("Enter n");		//there was a problem with my array references and I had no patience to resolve it.
		n=Integer.parseInt(input.readLine());	//so I did a little recomputation here.
		int n2=2*n;				//initially my program would create just one object and run till it found
							//but due to reinitialization problems..my array was scheduled for garbage collection
		int size=(n2*(n2-1))/2-n;		//if for attempt one it could find a solution then only would it exit else infinite loop

			if(size%3!=0)			//so i decided to delete the object and create a new object..its as if the program is being run again
			{				
			System.out.println("Not possible to form triples and cover all pairs uniquely");
			return;
			}

	
		for(int i=0;i<n;i++)		//this table gives you a nice look up on the men and women
		System.out.println(" Man = "+i+" Woman = "+(i+n));

		while(!stop)	//while the driver module does not return the right amount run instances of the program
		{
		DataOfTriple d=new DataOfTriple(n);
		stop=d.driver();
		}
	}
}