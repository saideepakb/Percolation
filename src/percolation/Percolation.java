/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package percolation;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import edu.princeton.cs.introcs.StdIn;
/**
 *
 * @author Deepak
 */
public final class Percolation {
   
    private WeightedQuickUnionUF uf; 
    private int[] openGrid;
    private int len;
    public int openCount = 0;
    
    /**
     *
     * @param N
     */
     
    public Percolation(int N) {
       
       if (N<=0) {
           throw new IllegalArgumentException("Grid size cannot be less than or equal to zero");
       }
       len = N;
       uf = new WeightedQuickUnionUF(N*N +2);
       openGrid = new int[N*N + 2]; //creating a N*N + 1 open grid so that we can start from 1 and go till N*N.
                                    // so that it corresponds to uf
       openGrid[0] = 1;
       openGrid[N*N+1] = 1;
       
       for (int k=1; k<=N*N; k++){
            if(StdRandom.uniform()>0.5) {
                linearToSet(k);                
                int[] ij = linearToSet(k);  
                
                int i = ij[0];
                int j = ij[1];
                this.open(i,j);
                this.openCount++;
            }
        }
    }
    
    //open a given cell
    
    private int[] linearToSet(int k) {
        int[] ij = new int[2];
        
        ij[0] = (k-1)/(len)+ 1;        
        ij[1] = (k-1)%len + 1;

        return ij;
    }
    
    private int setToLinear(int[] ij) {
       int k;
       k = (ij[0]-1)*len + ij[1];
       
       return k;
    }
    
    private void checkIllegalArgument(int i, int j) {
        if (i<=0 || i > uf.count() || j<=0 || j > uf.count()) {    
            throw new IllegalArgumentException("Illegal i is" + i + "; Illegal j is: " + j + "\t Uf.count is " + uf.count());
       }  
    }
    
    public void open(int i, int j) {
       
        checkIllegalArgument(i,j);
        
        int[] ij = {i, j};
 //       StdOut.println("i is" + ij[0]);
  //      StdOut.println("j is" + ij[1]);
        
        int k = setToLinear(ij);
  //      StdOut.println("k is" + k);
        openGrid[k]=1;
                
        if (indexExists(i, j-1)&& isOpen(i, j-1)) uf.union(len*(i-1)+j-1, k);
        if (indexExists(i, j+1)&& isOpen(i, j+1)) uf.union(len*(i-1)+j+1, k);
        if (indexExists(i-1, j)&& isOpen(i-1, j)) uf.union(len*(i-2)+j, k);
        if (indexExists(i+1, j)&& isOpen(i+1, j)) uf.union(len*(i)+j, k);
        
        if(i==1){
            uf.union(0, k);
        }
        if (i==len) {
            uf.union(len*len+1, k);
        }
    }
    
    private boolean indexExists(int i, int j){
        return !(i<1 || i>len || j<1 || j>len);
    }
    
    public boolean isOpen(int i, int j) {
        checkIllegalArgument(i,j); 
        return openGrid[(len)*(i-1)+j]==1;
    }
   
    public boolean isFull(int i, int j) {
        checkIllegalArgument(i, j);
        return uf.connected((len)*(i-1)+j, 0);
    }
    
    public boolean percolates() {
        return uf.connected(len*len+1, 0);
    }
    
    public static void main(String[] args) {
        int N = StdIn.readInt();
        Percolation pc = new Percolation(N);
        int n = 0;
        for (int i=1;i<=N*N;i++){
            StdOut.print(pc.openGrid[i] + "\t"); 
            if (i/N -n == 1) {
                StdOut.println("");
                n++;
            }
        }
        StdOut.println("\n Percolation: " + pc.percolates() + " \t Percolation Threshold  " + (double)(pc.openCount)/(N*N));
    }
}
