package com.code_n_droid.dwell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

class Customer {
    double lat, lon;
    int visited;

    Customer ( double lat , double lon ) {
        this.lat = lat;
        this.lon = lon;
    }

}

class TspDynamicProgrammingRecursive {

    private final int N;
    private final int START_NODE;
    private final int FINISHED_STATE;

    private double[][] distance;
    private double minTourCost = Double.POSITIVE_INFINITY;

    private List < Integer > tour = new ArrayList <> ();
    private boolean ranSolver = false;

    public TspDynamicProgrammingRecursive ( double[][] distance ) {
        this ( 0 , distance );
    }

    public TspDynamicProgrammingRecursive ( int startNode , double[][] distance ) {

        this.distance = distance;
        N = distance.length;
        START_NODE = startNode;

        // Validate inputs.
        //if (N <= 2) throw new IllegalStateException("TSP on 0, 1 or 2 nodes doesn't make sense.");
        if ( N != distance[ 0 ].length )
            throw new IllegalArgumentException ( "Matrix must be square (N x N)" );
        if ( START_NODE < 0 || START_NODE >= N )
            throw new IllegalArgumentException ( "Starting node must be: 0 <= startNode < N" );
        if ( N > 32 )
            throw new IllegalArgumentException (
                    "Matrix too large! A matrix that size for the DP TSP problem with a time complexity of"
                            + "O(n^2*2^n) requires way too much computation for any modern home computer to handle" );

        // The finished state is when the finished state mask has all bits are set to
        // one (meaning all the nodes have been visited).
        FINISHED_STATE = ( 1 << N ) - 1;
    }

    // Returns the optimal tour for the traveling salesman problem.
    public List < Integer > getTour ( ) {
        if ( ! ranSolver ) solve ();
        return tour;
    }

    // Returns the minimal tour cost.
    public double getTourCost ( ) {
        if ( ! ranSolver ) solve ();
        return minTourCost;
    }

    public void solve ( ) {

        // Run the solver
        int state = 1 << START_NODE;
        Double[][] memo = new Double[ N ][ 1 << N ];
        Integer[][] prev = new Integer[ N ][ 1 << N ];
        minTourCost = tsp ( START_NODE , state , memo , prev );

        // Regenerate Path
        int index = START_NODE;
        while ( true ) {
            tour.add ( index );
            Integer nextIndex = prev[ index ][ state ];
            if ( nextIndex == null ) break;
            int nextState = state | ( 1 << nextIndex );
            state = nextState;
            index = nextIndex;
        }
        tour.add ( START_NODE );
        ranSolver = true;
    }

    private double tsp ( int i , int state , Double[][] memo , Integer[][] prev ) {

        // Done this tour. Return cost of going back to start node.
        if ( state == FINISHED_STATE ) return distance[ i ][ START_NODE ];

        // Return cached answer if already computed.
        if ( memo[ i ][ state ] != null ) return memo[ i ][ state ];

        double minCost = Double.POSITIVE_INFINITY;
        int index = - 1;
        for ( int next = 0 ; next < N ; next++ ) {

            // Skip if the next node has already been visited.
            if ( ( state & ( 1 << next ) ) != 0 ) continue;

            int nextState = state | ( 1 << next );
            double newCost = distance[ i ][ next ] + tsp ( next , nextState , memo , prev );
            if ( newCost < minCost ) {
                minCost = newCost;
                index = next;
            }
        }

        prev[ i ][ state ] = index;
        return memo[ i ][ state ] = minCost;
    }
}

public class Path {
    int quad = - 1;
    double[] start = {0 , 0};
    double[] origin = {0 , 0};
    ArrayList < double[] > first;
    ArrayList < double[] > second;
    ArrayList < double[] > third;
    ArrayList < double[] > fourth;
    ArrayList < Customer > arr;
    HashMap < Integer, double[] > clockwise = new HashMap <> ();
    HashMap < Integer, double[] > anticlockwise = new HashMap <> ();
    HashSet < Double > visited = new HashSet <> ();
    int total;


    // generic function to calculate the nearest point from two different quadrants
    public double[] nearest ( ArrayList < double[] > first , ArrayList < double[] > second ) {
        double array[] = {0 , 0 , 0 , 0};
        double distance = Integer.MAX_VALUE;
        for ( int i = 0 ; i < first.size () ; i++ ) {
            for ( int j = 0 ; j < second.size () ; j++ ) {
                double dis = Math.pow ( first.get ( i )[ 0 ] - second.get ( j )[ 0 ] , 2 ) + Math.pow ( first.get ( i )[ 1 ] - second.get ( j )[ 1 ] , 2 );
                if ( distance > dis ) {
                    array[ 0 ] = first.get ( i )[ 0 ];
                    array[ 1 ] = first.get ( i )[ 1 ];
                    array[ 2 ] = second.get ( j )[ 0 ];
                    array[ 3 ] = second.get ( j )[ 1 ];
                    distance = dis;
                }
            }
        }
        return array;
    }

    public double[][] list_to_matrix ( ArrayList < double[] > first ) {
        double[][] dis = new double[ first.size () ][ first.size () ];
        for ( int i = 0 ; i < first.size () ; i++ ) {
            for ( int j = 0 ; j < first.size () ; j++ ) {
                if ( i != j ) {
                    double distance = Math.pow ( first.get ( i )[ 0 ] - first.get ( j )[ 0 ] , 2 ) + Math.pow ( first.get ( i )[ 1 ] - first.get ( j )[ 1 ] , 2 );
                    dis[ i ][ j ] = dis[ j ][ i ] = distance;
                }
            }
        }
        return dis;
    }

    public int travel ( ) {
        double shortest_distance = 0;
        int x = 0;
        // clockwise travel
        for ( int i = 0 ; i < 4 ; i++ ) {
            if ( quad == 4 ) {
                double[] dest = nearest ( fourth , third );
                double[] remove = {dest[ 0 ] , dest[ 1 ]};
                fourth.remove ( remove );
                double[][] dis = list_to_matrix ( fourth );
                TspDynamicProgrammingRecursive solver = new TspDynamicProgrammingRecursive ( dis );
                List < Integer > list = solver.getTour ();
                int n = list.size ();
                double del = Math.pow ( fourth.get ( list.get ( n - 2 ) )[ 0 ] - fourth.get ( list.get ( n - 1 ) )[ 0 ] , 2 ) + Math.pow ( fourth.get ( list.get ( n - 2 ) )[ 1 ] - fourth.get ( list.get ( n - 1 ) )[ 1 ] , 2 );
                double add = Math.pow ( fourth.get ( list.get ( 0 ) )[ 0 ] - start[ 0 ] , 2 ) + Math.pow ( fourth.get ( list.get ( 0 ) )[ 1 ] - start[ 1 ] , 2 );
                double add2 = Math.pow ( fourth.get ( list.get ( n - 2 ) )[ 0 ] - dest[ 0 ] , 2 ) + Math.pow ( fourth.get ( list.get ( n - 2 ) )[ 1 ] - dest[ 1 ] , 2 );
                shortest_distance += add + add2 - del;
                shortest_distance += solver.getTourCost ();
                for ( int z : list ) {
                    if ( visited.add ( fourth.get ( z )[ 0 ] ) ) {
                        double[] p = {fourth.get ( z )[ 0 ] , fourth.get ( z )[ 1 ]};
                        clockwise.put ( x , p );
                        x++;
                    }

                }
                visited.add ( remove[ 0 ] );
                clockwise.put ( x , remove );
                x++;
                if ( x < total ) {
                    visited.add ( dest[ 2 ] );
                    double[] k = {dest[ 2 ] , dest[ 3 ]};
                    clockwise.put ( x , k );
                    x++;
                }
                start[ 0 ] = dest[ 2 ];
                start[ 1 ] = dest[ 3 ];
                shortest_distance += Math.pow ( dest[ 0 ] - dest[ 2 ] , 2 ) + Math.pow ( dest[ 1 ] - dest[ 3 ] , 2 );
                quad--;
            } else if ( quad == 3 ) {
                double[] dest = nearest ( third , second );
                double[] remove = {dest[ 0 ] , dest[ 1 ]};
                third.remove ( remove );
                double[][] dis = list_to_matrix ( third );
                TspDynamicProgrammingRecursive solver = new TspDynamicProgrammingRecursive ( dis );
                List < Integer > list = solver.getTour ();
                int n = list.size ();
                double del = Math.pow ( third.get ( list.get ( n - 2 ) )[ 0 ] - third.get ( list.get ( n - 1 ) )[ 0 ] , 2 ) + Math.pow ( third.get ( list.get ( n - 2 ) )[ 1 ] - third.get ( list.get ( n - 1 ) )[ 1 ] , 2 );
                double add = Math.pow ( third.get ( list.get ( 0 ) )[ 0 ] - start[ 0 ] , 2 ) + Math.pow ( third.get ( list.get ( 0 ) )[ 1 ] - start[ 1 ] , 2 );
                double add2 = Math.pow ( third.get ( list.get ( n - 2 ) )[ 0 ] - dest[ 0 ] , 2 ) + Math.pow ( third.get ( list.get ( n - 2 ) )[ 1 ] - dest[ 1 ] , 2 );
                shortest_distance += add + add2 - del;
                shortest_distance += solver.getTourCost ();
                for ( int z : list ) {
                    if ( visited.add ( third.get ( z )[ 0 ] ) ) {
                        double[] p = {third.get ( z )[ 0 ] , third.get ( z )[ 1 ]};
                        clockwise.put ( x , p );
                        x++;
                    }

                }
                visited.add ( remove[ 0 ] );
                clockwise.put ( x , remove );
                x++;
                if ( x < total ) {
                    visited.add ( dest[ 2 ] );
                    double[] k = {dest[ 2 ] , dest[ 3 ]};
                    clockwise.put ( x , k );
                    x++;
                }
                start[ 0 ] = dest[ 2 ];
                start[ 1 ] = dest[ 3 ];
                shortest_distance += Math.pow ( dest[ 0 ] - dest[ 2 ] , 2 ) + Math.pow ( dest[ 1 ] - dest[ 3 ] , 2 );
                quad--;
            } else if ( quad == 2 ) {
                double[] dest = nearest ( second , first );
                double[] remove = {dest[ 0 ] , dest[ 1 ]};
                second.remove ( remove );
                double[][] dis = list_to_matrix ( second );
                TspDynamicProgrammingRecursive solver = new TspDynamicProgrammingRecursive ( dis );
                List < Integer > list = solver.getTour ();
                int n = list.size ();
                double del = Math.pow ( second.get ( list.get ( n - 2 ) )[ 0 ] - second.get ( list.get ( n - 1 ) )[ 0 ] , 2 ) + Math.pow ( second.get ( list.get ( n - 2 ) )[ 1 ] - second.get ( list.get ( n - 1 ) )[ 1 ] , 2 );
                double add = Math.pow ( second.get ( list.get ( 0 ) )[ 0 ] - start[ 0 ] , 2 ) + Math.pow ( second.get ( list.get ( 0 ) )[ 1 ] - start[ 1 ] , 2 );
                double add2 = Math.pow ( second.get ( list.get ( n - 2 ) )[ 0 ] - dest[ 0 ] , 2 ) + Math.pow ( second.get ( list.get ( n - 2 ) )[ 1 ] - dest[ 1 ] , 2 );
                shortest_distance += add + add2 - del;
                shortest_distance += solver.getTourCost ();
                for ( int z : list ) {
                    if ( visited.add ( second.get ( z )[ 0 ] ) ) {
                        double[] p = {second.get ( z )[ 0 ] , second.get ( z )[ 1 ]};
                        clockwise.put ( x , p );
                        x++;
                    }

                }
                visited.add ( remove[ 0 ] );
                clockwise.put ( x , remove );
                x++;
                if ( x < total ) {
                    visited.add ( dest[ 2 ] );
                    double[] k = {dest[ 2 ] , dest[ 3 ]};
                    clockwise.put ( x , k );
                    x++;
                }
                start[ 0 ] = dest[ 2 ];
                start[ 1 ] = dest[ 3 ];
                shortest_distance += Math.pow ( dest[ 0 ] - dest[ 2 ] , 2 ) + Math.pow ( dest[ 1 ] - dest[ 3 ] , 2 );
                quad--;
            } else {
                double[] dest = nearest ( first , second );
                double[] remove = {dest[ 0 ] , dest[ 1 ]};
                first.remove ( remove );
                double[][] dis = list_to_matrix ( first );
                TspDynamicProgrammingRecursive solver = new TspDynamicProgrammingRecursive ( dis );
                List < Integer > list = solver.getTour ();
                int n = list.size ();
                double del = Math.pow ( first.get ( list.get ( n - 2 ) )[ 0 ] - first.get ( list.get ( n - 1 ) )[ 0 ] , 2 ) + Math.pow ( first.get ( list.get ( n - 2 ) )[ 1 ] - first.get ( list.get ( n - 1 ) )[ 1 ] , 2 );
                double add = Math.pow ( first.get ( list.get ( 0 ) )[ 0 ] - start[ 0 ] , 2 ) + Math.pow ( first.get ( list.get ( 0 ) )[ 1 ] - start[ 1 ] , 2 );
                double add2 = Math.pow ( first.get ( list.get ( n - 2 ) )[ 0 ] - dest[ 0 ] , 2 ) + Math.pow ( first.get ( list.get ( n - 2 ) )[ 1 ] - dest[ 1 ] , 2 );
                shortest_distance += add + add2 - del;
                shortest_distance += solver.getTourCost ();
                for ( int z : list ) {
                    if ( visited.add ( first.get ( z )[ 0 ] ) ) {
                        double[] p = {first.get ( z )[ 0 ] , first.get ( z )[ 1 ]};
                        clockwise.put ( x , p );
                        x++;
                    }

                }
                visited.add ( remove[ 0 ] );
                clockwise.put ( x , remove );
                x++;
                if ( x < total ) {
                    visited.add ( dest[ 2 ] );
                    double[] k = {dest[ 2 ] , dest[ 3 ]};
                    clockwise.put ( x , k );
                    x++;
                }
                start[ 0 ] = dest[ 2 ];
                start[ 1 ] = dest[ 3 ];
                shortest_distance += Math.pow ( dest[ 0 ] - dest[ 2 ] , 2 ) + Math.pow ( dest[ 1 ] - dest[ 3 ] , 2 );
                quad = 4;
            }
        }


        x = 0;
        // anticlockwise travel
        double shortest_distance2 = 0;
        for ( int i = 0 ; i < 4 ; i++ ) {
            if ( quad == 4 ) {
                double[] dest = nearest ( fourth , third );
                double[] remove = {dest[ 0 ] , dest[ 1 ]};
                fourth.remove ( remove );
                double[][] dis = list_to_matrix ( fourth );
                TspDynamicProgrammingRecursive solver = new TspDynamicProgrammingRecursive ( dis );
                List < Integer > list = solver.getTour ();
                int n = list.size ();
                double del = Math.pow ( fourth.get ( list.get ( n - 2 ) )[ 0 ] - fourth.get ( list.get ( n - 1 ) )[ 0 ] , 2 ) + Math.pow ( fourth.get ( list.get ( n - 2 ) )[ 1 ] - fourth.get ( list.get ( n - 1 ) )[ 1 ] , 2 );
                double add = Math.pow ( fourth.get ( list.get ( 0 ) )[ 0 ] - start[ 0 ] , 2 ) + Math.pow ( fourth.get ( list.get ( 0 ) )[ 1 ] - start[ 1 ] , 2 );
                double add2 = Math.pow ( fourth.get ( list.get ( n - 2 ) )[ 0 ] - dest[ 0 ] , 2 ) + Math.pow ( fourth.get ( list.get ( n - 2 ) )[ 1 ] - dest[ 1 ] , 2 );
                shortest_distance += add + add2 - del;
                shortest_distance += solver.getTourCost ();
                for ( int z : list ) {
                    if ( visited.add ( fourth.get ( z )[ 0 ] ) ) {
                        double[] p = {fourth.get ( z )[ 0 ] , fourth.get ( z )[ 1 ]};
                        anticlockwise.put ( x , p );
                        x++;
                    }

                }
                visited.add ( remove[ 0 ] );
                anticlockwise.put ( x , remove );
                x++;
                if ( x < total ) {
                    visited.add ( dest[ 2 ] );
                    double[] k = {dest[ 2 ] , dest[ 3 ]};
                    anticlockwise.put ( x , k );
                    x++;
                }
                start[ 0 ] = dest[ 2 ];
                start[ 1 ] = dest[ 3 ];
                shortest_distance2 += Math.pow ( dest[ 0 ] - dest[ 2 ] , 2 ) + Math.pow ( dest[ 1 ] - dest[ 3 ] , 2 );
                quad = 1;
            } else if ( quad == 3 ) {
                double[] dest = nearest ( third , second );
                double[] remove = {dest[ 0 ] , dest[ 1 ]};
                third.remove ( remove );
                double[][] dis = list_to_matrix ( third );
                TspDynamicProgrammingRecursive solver = new TspDynamicProgrammingRecursive ( dis );
                List < Integer > list = solver.getTour ();
                int n = list.size ();
                double del = Math.pow ( third.get ( list.get ( n - 2 ) )[ 0 ] - third.get ( list.get ( n - 1 ) )[ 0 ] , 2 ) + Math.pow ( third.get ( list.get ( n - 2 ) )[ 1 ] - third.get ( list.get ( n - 1 ) )[ 1 ] , 2 );
                double add = Math.pow ( third.get ( list.get ( 0 ) )[ 0 ] - start[ 0 ] , 2 ) + Math.pow ( third.get ( list.get ( 0 ) )[ 1 ] - start[ 1 ] , 2 );
                double add2 = Math.pow ( third.get ( list.get ( n - 2 ) )[ 0 ] - dest[ 0 ] , 2 ) + Math.pow ( third.get ( list.get ( n - 2 ) )[ 1 ] - dest[ 1 ] , 2 );
                shortest_distance += add + add2 - del;
                shortest_distance += solver.getTourCost ();
                for ( int z : list ) {
                    if ( visited.add ( third.get ( z )[ 0 ] ) ) {
                        double[] p = {third.get ( z )[ 0 ] , third.get ( z )[ 1 ]};
                        anticlockwise.put ( x , p );
                        x++;
                    }

                }
                visited.add ( remove[ 0 ] );
                anticlockwise.put ( x , remove );
                x++;
                if ( x < total ) {
                    visited.add ( dest[ 2 ] );
                    double[] k = {dest[ 2 ] , dest[ 3 ]};
                    anticlockwise.put ( x , k );
                    x++;
                }
                start[ 0 ] = dest[ 2 ];
                start[ 1 ] = dest[ 3 ];
                shortest_distance2 += Math.pow ( dest[ 0 ] - dest[ 2 ] , 2 ) + Math.pow ( dest[ 1 ] - dest[ 3 ] , 2 );
                quad++;
            } else if ( quad == 2 ) {
                double[] dest = nearest ( second , first );
                double[] remove = {dest[ 0 ] , dest[ 1 ]};
                second.remove ( remove );
                double[][] dis = list_to_matrix ( second );
                TspDynamicProgrammingRecursive solver = new TspDynamicProgrammingRecursive ( dis );
                List < Integer > list = solver.getTour ();
                int n = list.size ();
                double del = Math.pow ( second.get ( list.get ( n - 2 ) )[ 0 ] - second.get ( list.get ( n - 1 ) )[ 0 ] , 2 ) + Math.pow ( second.get ( list.get ( n - 2 ) )[ 1 ] - second.get ( list.get ( n - 1 ) )[ 1 ] , 2 );
                double add = Math.pow ( second.get ( list.get ( 0 ) )[ 0 ] - start[ 0 ] , 2 ) + Math.pow ( second.get ( list.get ( 0 ) )[ 1 ] - start[ 1 ] , 2 );
                double add2 = Math.pow ( second.get ( list.get ( n - 2 ) )[ 0 ] - dest[ 0 ] , 2 ) + Math.pow ( second.get ( list.get ( n - 2 ) )[ 1 ] - dest[ 1 ] , 2 );
                shortest_distance += add + add2 - del;
                shortest_distance += solver.getTourCost ();
                for ( int z : list ) {
                    if ( visited.add ( second.get ( z )[ 0 ] ) ) {
                        double[] p = {second.get ( z )[ 0 ] , second.get ( z )[ 1 ]};
                        anticlockwise.put ( x , p );
                        x++;
                    }

                }
                visited.add ( remove[ 0 ] );
                anticlockwise.put ( x , remove );
                x++;
                if ( x < total ) {
                    visited.add ( dest[ 2 ] );
                    double[] k = {dest[ 2 ] , dest[ 3 ]};
                    anticlockwise.put ( x , k );
                    x++;
                }
                start[ 0 ] = dest[ 2 ];
                start[ 1 ] = dest[ 3 ];
                shortest_distance2 += Math.pow ( dest[ 0 ] - dest[ 2 ] , 2 ) + Math.pow ( dest[ 1 ] - dest[ 3 ] , 2 );
                quad++;
            } else {
                double[] dest = nearest ( first , second );
                double[] remove = {dest[ 0 ] , dest[ 1 ]};
                first.remove ( remove );
                double[][] dis = list_to_matrix ( first );
                TspDynamicProgrammingRecursive solver = new TspDynamicProgrammingRecursive ( dis );
                List < Integer > list = solver.getTour ();
                int n = list.size ();
                double del = Math.pow ( first.get ( list.get ( n - 2 ) )[ 0 ] - first.get ( list.get ( n - 1 ) )[ 0 ] , 2 ) + Math.pow ( first.get ( list.get ( n - 2 ) )[ 1 ] - first.get ( list.get ( n - 1 ) )[ 1 ] , 2 );
                double add = Math.pow ( first.get ( list.get ( 0 ) )[ 0 ] - start[ 0 ] , 2 ) + Math.pow ( first.get ( list.get ( 0 ) )[ 1 ] - start[ 1 ] , 2 );
                double add2 = Math.pow ( first.get ( list.get ( n - 2 ) )[ 0 ] - dest[ 0 ] , 2 ) + Math.pow ( first.get ( list.get ( n - 2 ) )[ 1 ] - dest[ 1 ] , 2 );
                shortest_distance += add + add2 - del;
                shortest_distance += solver.getTourCost ();
                for ( int z : list ) {
                    if ( visited.add ( first.get ( z )[ 0 ] ) ) {
                        double[] p = {first.get ( z )[ 0 ] , first.get ( z )[ 1 ]};
                        anticlockwise.put ( x , p );
                        x++;
                    }

                }
                visited.add ( remove[ 0 ] );
                anticlockwise.put ( x , remove );
                x++;
                if ( x < total ) {
                    visited.add ( dest[ 2 ] );
                    double[] k = {dest[ 2 ] , dest[ 3 ]};
                    anticlockwise.put ( x , k );
                    x++;
                }
                start[ 0 ] = dest[ 2 ];
                start[ 1 ] = dest[ 3 ];
                shortest_distance2 += Math.pow ( dest[ 0 ] - dest[ 2 ] , 2 ) + Math.pow ( dest[ 1 ] - dest[ 3 ] , 2 );
                quad++;
            }


        }
        return ( shortest_distance > shortest_distance2 ) ? 2 : 1;
    }

    public void quad ( ) {
        if ( start[ 0 ] < origin[ 0 ] ) {
            if ( start[ 1 ] <= origin[ 1 ] )
                quad = 3;
            else
                quad = 2;

        } else {
            if ( start[ 1 ] <= origin[ 1 ] )
                quad = 4;
            else
                quad = 1;
        }
    }

    public void origins ( ) {
        for ( int i = 0 ; i < arr.size () ; i++ ) {
            origin[ 0 ] += arr.get ( i ).lat;
            origin[ 1 ] += arr.get ( i ).lon;
        }
        origin[ 0 ] /= arr.size ();
        origin[ 1 ] /= arr.size ();
    }

    public void store ( ) {
        first = new ArrayList <> ();
        second = new ArrayList <> ();
        third = new ArrayList <> ();
        fourth = new ArrayList <> ();
        for ( int i = 0 ; i < arr.size () ; i++ ) {
            double[] x = {arr.get ( i ).lat , arr.get ( i ).lon};
            if ( x[ 0 ] <= origin[ 0 ] && x[ 1 ] < origin[ 1 ] )
                third.add ( x );
            else if ( x[ 0 ] > origin[ 0 ] && x[ 1 ] <= origin[ 1 ] )
                fourth.add ( x );
            else if ( x[ 0 ] >= origin[ 0 ] && x[ 1 ] > origin[ 1 ] )
                first.add ( x );
            else
                second.add ( x );
        }
    }

    public void rank ( Data data, double lat1, double lon1 ) {


        Path obj = new Path ();
        obj.start = new double[]{lat1 , lon1};
        // obj.arr = new ArrayList<Customer>(20);
        // for(int i = 0;i<20;i++)
        // {
        //     double lat = Math.random()*100;
        //     double lon = Math.random()*100;
        //     obj.arr.add(new Customer(lat,lon));
        //     System.out.println(lat+" "+lon);
        // }
        // obj.origins();
        // obj.quad();
        // obj.store();
        // int d = obj.travel();
        // System.out.println(d);
        // for(int j = 0;j<obj.clockwise.size();j++)
        //  {    double [] p = obj.clockwise.get(j);
        //      System.out.println(j+" "+p[0]+" "+p[1]);
        //  }
        obj.arr = new ArrayList <> ( data.getCustomerDetails ().size () );
        for ( int i = 0 ; i < data.getCustomerDetails ().size () ; i++ ) {
            double lat = Double.parseDouble ( data.getCustomerDetails ().get ( i ).getCustomerAddress ().getLatLong ().getLatitude () );
            double lon = Double.parseDouble ( data.getCustomerDetails ().get ( i ).getCustomerAddress ().getLatLong ().getLongitude () );
            data.getCustomerDetails ().get ( i ).getCustomerAddress ().getLatLong ().setRank ( - 1 );
            obj.arr.add ( new Customer ( lat , lon ) );
        }
        obj.origins ();
        obj.quad ();
        obj.store ();
        obj.total = obj.arr.size ();
        int d = obj.travel ();
        if ( d == 2 ) {
            for ( int i = 0 ; i < data.getCustomerDetails ().size () ; i++ )
                for ( int j = 0 ; j < obj.clockwise.size () ; j++ ) {
                    double[] p = obj.clockwise.get ( j );
                    double lat = Double.parseDouble ( data.getCustomerDetails ().get ( i ).getCustomerAddress ().getLatLong ().getLatitude () );
                    double lon = Double.parseDouble ( data.getCustomerDetails ().get ( i ).getCustomerAddress ().getLatLong ().getLongitude () );
                    int rank = data.getCustomerDetails ().get ( i ).getCustomerAddress ().getLatLong ().getRank ();
                    if ( p[ 0 ] == lat && p[ 1 ] == lon && rank == - 1 )
                        data.getCustomerDetails ().get ( i ).getCustomerAddress ().getLatLong ().setRank ( j );
                }
        } else
            for ( int i = 0 ; i < data.getCustomerDetails ().size () ; i++ )
                for ( int j = 0 ; j < obj.anticlockwise.size () ; j++ ) {
                    double[] p = obj.anticlockwise.get ( j );
                    double lat = Double.parseDouble ( data.getCustomerDetails ().get ( i ).getCustomerAddress ().getLatLong ().getLatitude () );
                    double lon = Double.parseDouble ( data.getCustomerDetails ().get ( i ).getCustomerAddress ().getLatLong ().getLongitude () );
                    int rank = data.getCustomerDetails ().get ( i ).getCustomerAddress ().getLatLong ().getRank ();
                    if ( p[ 0 ] == lat && p[ 1 ] == lon && rank == - 1 )
                        data.getCustomerDetails ().get ( i ).getCustomerAddress ().getLatLong ().setRank ( j );
                }


    }

}
 