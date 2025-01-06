package venom;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * The Venom class represents a binary search tree of SymbioteHost objects.
 * Venom is a sentient alien symbiote with a liquid-like form that requires a
 * host to bond with for its survival. The host is granted superhuman abilities
 * and the symbiote gains a degree of autonomy. The Venom class contains methods
 * that will help venom find the most compatible host. You are Venom.
 * 
 * @author Ayla Muminovic
 * @author Shane Haughton
 * @author Elian D. Deogracia-Brito
 */
public class Venom {
    private SymbioteHost root;

    /**
     * DO NOT EDIT THIS METHOD
     * 
     * Default constructor.
     */
    public Venom() {
        root = null;
    }

    /**
     * This method is provided to you
     * Creates an array of SymbioteHost objects from a file. The file should
     * contain the number of people on the first line, followed by the name,
     * compatibility, stability, and whether they have antibodies on each
     * subsequent line.
     * 
     * @param filename the name of the file
     * @return an array of SymbioteHosts (should not contain children)
     */
    public SymbioteHost[] createSymbioteHosts(String filename) {
        // DO NOT EDIT THIS METHOD
        StdIn.setFile(filename);
        int numOfPeople = StdIn.readInt();
        SymbioteHost[] people = new SymbioteHost[numOfPeople];
        for (int i = 0; i < numOfPeople; i++) {
            StdIn.readLine();
            String name = StdIn.readLine();
            int compatibility = StdIn.readInt();
            int stability = StdIn.readInt();
            boolean hasAntibodies = StdIn.readBoolean();
            SymbioteHost newPerson = new SymbioteHost(name, compatibility, stability, hasAntibodies, null, null);
            people[i] = newPerson;
        }
        return people;
    }

    /**
     * Inserts a SymbioteHost object into the binary search tree.
     * 
     * @param symbioteHost the SymbioteHost object to insert
     */
    public void insertSymbioteHost(SymbioteHost symbioteHost) {
        root = insertSymbioteHost(root, symbioteHost);
    }

    private SymbioteHost insertSymbioteHost(SymbioteHost current, SymbioteHost symbioteHost) {
        if (current == null) {return symbioteHost;}

        int cmp = symbioteHost.getName().compareTo(current.getName());

        if (cmp < 0) {
            current.setLeft(insertSymbioteHost(current.getLeft(), symbioteHost));
        } else if (cmp > 0) {
            current.setRight(insertSymbioteHost(current.getRight(), symbioteHost));
        } else {
            current.setSymbioteCompatibility(symbioteHost.getSymbioteCompatibility());
            current.setMentalStability(symbioteHost.getMentalStability());
            current.setHasAntibodies(symbioteHost.hasAntibodies());
        }
        return current;
    }

    /**
     * Builds a binary search tree from an array of SymbioteHost objects.
     * 
     * @param filename filename to read
     */
    public void buildTree(String filename) {
        // WRITE YOUR CODE HERE
        SymbioteHost[] peoples = createSymbioteHosts(filename);
        for (SymbioteHost symbioteHost : peoples) {
            insertSymbioteHost(symbioteHost);
        }
    }

    /**
     * Finds the most compatible host in the tree. The most compatible host
     * is the one with the highest suitability.
     * PREorder traversal is used to traverse the tree. The host with the highest suitability
     * is returned. If the tree is empty, null is returned.
     * 
     * USE the calculateSuitability method on a SymbioteHost instance to get
     * a host's suitability.
     * 
     * @return the most compatible SymbioteHost object
     */
    public SymbioteHost findMostSuitable() {
        return findMostSuitableHost(root, root);
    }

    private SymbioteHost findMostSuitableHost(SymbioteHost current, SymbioteHost mostSuitable) {
        if (current == null) {
            return mostSuitable;
        }
        if (current.calculateSuitability() > mostSuitable.calculateSuitability()) {
            mostSuitable = current;
        }
        mostSuitable = findMostSuitableHost(current.getLeft(), mostSuitable);
        mostSuitable = findMostSuitableHost(current.getRight(), mostSuitable);

        return mostSuitable;
    }

    /**
     * Finds all hosts in the tree that have antibodies. INorder traversal is used to
     * traverse the tree. The hosts that have antibodies are added to an
     * ArrayList. If the tree is empty, null is returned.
     * 
     * @return an ArrayList of SymbioteHost objects that have antibodies
     */
    public ArrayList<SymbioteHost> findHostsWithAntibodies() {
        // WRITE YOUR CODE HERE

        ArrayList<SymbioteHost> AntibodiesHosts = new ArrayList<>();
        findHostsWithAntibodies(root, AntibodiesHosts);
        return AntibodiesHosts;
    }
    private void findHostsWithAntibodies(SymbioteHost current, ArrayList<SymbioteHost> Antibodies) {
        if (current == null) {return;}

        findHostsWithAntibodies(current.getLeft(), Antibodies);

        if (current.hasAntibodies()) {
            Antibodies.add(current);
        }
        findHostsWithAntibodies(current.getRight(), Antibodies);
    }

    /**
     * Finds all hosts in the tree that have a suitability between the given
     * range. The range is inclusive. Level order traversal is used to traverse the tree. The
     * hosts that fall within the range are added to an ArrayList. If the tree
     * is empty, null is returned.
     * 
     * @param minSuitability the minimum suitability
     * @param maxSuitability the maximum suitability
     * @return an ArrayList of SymbioteHost objects that fall within the range
     */
    public ArrayList<SymbioteHost> findHostsWithinSuitabilityRange(int minSuitability, int maxSuitability) {
        ArrayList<SymbioteHost> AntibodiesHosts = new ArrayList<>();
        if (root == null) {return AntibodiesHosts;}

        Queue<SymbioteHost> queue = new Queue<>();
        queue.enqueue(root);

        while (!queue.isEmpty()) {
            SymbioteHost current = queue.dequeue();

            int suitability = current.calculateSuitability();

            if (suitability >= minSuitability && suitability <= maxSuitability) {
                AntibodiesHosts.add(current);
            }

            if (current.getLeft() != null) {
                queue.enqueue(current.getLeft());
            }
            if (current.getRight() != null) {
                queue.enqueue(current.getRight());
            }
        }
        return AntibodiesHosts;
    }


    /**
     * Deletes a node from the binary search tree with the given name.
     * If the node is not found, nothing happens.
     * 
     * @param name the name of the SymbioteHost object to delete
     */
    public void deleteSymbioteHost(String name) {
        // WRITE YOUR CODE HERE
        root = deleteSymbioteHost(root, name);
    }

    private SymbioteHost deleteSymbioteHost(SymbioteHost current, String name) {
        if (current == null) {return null;}

        int cmp = name.compareTo(current.getName());
        if (cmp < 0) {
            current.setLeft(deleteSymbioteHost(current.getLeft(), name));
        }
        else if (cmp > 0) {
            current.setRight(deleteSymbioteHost(current.getRight(), name));
        }
        else {
            if (current.getLeft() == null && current.getRight() == null) {return null;}
            if (current.getLeft() == null) {return current.getRight();}
            if (current.getRight() == null) {return current.getLeft();}

            SymbioteHost temp = GetMin(current.getRight());
            current.setName(temp.getName());
            current.setSymbioteCompatibility(temp.getSymbioteCompatibility());
            current.setMentalStability(temp.getMentalStability());
            current.setHasAntibodies(temp.hasAntibodies());
            current.setRight(deleteSymbioteHost(current.getRight(), temp.getName()));
        }
        return current;
    }

    private SymbioteHost GetMin(SymbioteHost current) {
        while (current.getLeft() != null) {
            current = current.getLeft();
        }
        return current;
    }

    /**
     * Challenge - worth zero points
     *
     * Heroes have arrived to defeat you! You must clean up the tree to
     * optimize your chances of survival. You must remove hosts with a
     * suitability between 0 and 100 and hosts that have antibodies.
     * 
     * Cleans up the tree by removing nodes with a suitability of 0 to 100
     * and nodes that have antibodies (IN THAT ORDER).
     */
    public void cleanupTree() {
        // WRITE YOUR CODE HERE
        ArrayList<SymbioteHost> AntibodiesHosts = new ArrayList<>();
        ArrayList<SymbioteHost> SuitabilityHosts = new ArrayList<>();

        AntibodiesHosts = findHostsWithAntibodies();
        SuitabilityHosts = findHostsWithinSuitabilityRange(0, 100);

        for (SymbioteHost host : AntibodiesHosts) {
            deleteSymbioteHost(host.getName());
        }

        for (SymbioteHost host : SuitabilityHosts) {
            deleteSymbioteHost(host.getName());
        }
    }

    /**
     * Gets the root of the tree.
     * 
     * @return the root of the tree
     */
    public SymbioteHost getRoot() {
        return root;
    }

    /**
     * Prints out the tree.
     */
    public void printTree() {
        if (root == null) {
            return;
        }

        // Modify no. of '\t' based on depth of node
        printTree(root, 0, false, false);
    }

    private void printTree(SymbioteHost root, int depth, boolean isRight, boolean isLeft) {
        System.out.print("\t".repeat(depth));

        if (isRight) {
            System.out.print("|-R- ");
        } else if (isLeft) {
            System.out.print("|-L- ");
        } else {
            System.out.print("+--- ");
        }

        if (root == null) {
            System.out.println("null");
            return;
        }

        System.out.println(root);

        if (root.getLeft() == null && root.getRight() == null) {
            return;
        }

        printTree(root.getLeft(), depth + 1, false, true);
        printTree(root.getRight(), depth + 1, true, false);
    }
}
