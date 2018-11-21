package Overview;


public class Combinatorics {

	int k;
	int n;
	int[] arr;
	int[] data;
		
	public Combinatorics(int n, int k)
	{
		this.n = n;
		this.k = k;
		int[] arr = new int[n]; 
		for (int i=0;i<n;i++)
		{
			arr[i] = i+1;
		}
		this.arr = arr;		
		int[] data = new int[k];
		this.data = data;
	}
	
	public void printCombinatorics()
	{
		combinationUtil(this.arr,this.n,this.k,0,this.data,0);
	}
	
	public static void combinationUtil(int arr[], int n, int k, int index, int[] data, int i)
	{
		if (index == k)
		{
			for (int j=0;j<k;j++)
			{
				System.out.print(data[j]+" ");
			}
			System.out.println("");
			return;
		}
		
		if (i>=n)
		{
			return;
		}
		
		data[index] = arr[i];
		combinationUtil(arr, n, k, index+1, data, i+1);
		combinationUtil(arr, n, k, index, data, i+1);
	}	
}

