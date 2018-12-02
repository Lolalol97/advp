package base.solution;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import base.Person;
import base.Solver;
import base.Task;
import base.World;
import base.eq.TimeEquivalenceKey;

public class FairNumSolver implements Solver {
    private World world;
    private boolean computePartialSolutionWhenSolvingNotPossible;
    //private boolean randomize; //TODO evtl.
    
    public FairNumSolver() {
        this.world = null;
        this.computePartialSolutionWhenSolvingNotPossible = false;
    }
    
    public FairNumSolver(boolean computePartialSolutionWhenSolvingNotPossible) {
        this.world = null;
        this.computePartialSolutionWhenSolvingNotPossible = computePartialSolutionWhenSolvingNotPossible;
    }
    
    public boolean doesComputePartialSolutionWhenSolvingNotPossible() {
        return this.computePartialSolutionWhenSolvingNotPossible;
    }
    
    public void setComputePartialSolutionWhenSolvingNotPossible(boolean computePartialSolutionWhenSolvingNotPossible) {
        this.computePartialSolutionWhenSolvingNotPossible = computePartialSolutionWhenSolvingNotPossible;
    }
    
    @Override
    public void setWorld(World world) {
        this.world = world;
    }

    @Override
    public boolean solve() {
        if(world != null) {
            //time-complexity: O(|Task| * |Person|^2 * (max |TaskProperties|) * (max |PersonProperties|) )
            //so if all values are ca. of the same length n: O(n^5)
            
            final List<Task> tasks = new ArrayList<>(world.getTasks());
            final List<Person> persons = new ArrayList<>(world.getPersons());
            final Map<Person, Integer> personToNumMap = new HashMap<>();
            persons.forEach(x -> personToNumMap.put(x, 0));
            
            for(final Task task : tasks) {
                boolean taskMapped = false;
                final Set<Person> alreadyTried = new HashSet<>();
                final Set<Person> alreadyMapped = new HashSet<>();
                Person person = getNextPerson(personToNumMap, alreadyTried);
                while(person != null && !taskMapped) {
                    boolean mapped = alreadyMapped.contains(person) ? false : world.mapTaskInstanceToPerson(task, person);
                    
                    //////////////////////////////////////////////////////////////
                    
                    //TODO: DEBUG-Einspringpunkt entfernen sobald fehler gefunden
                    /*
                    try {
                        if(task.getName().equals("Floor 1")
                                &&
                                task.getProperties()
                                .stream()
                                .filter(x -> x.getEquivalenceKey().getClass().equals(TimeEquivalenceKey.class))
                                .map(x -> TimeEquivalenceKey.class.cast(x.getEquivalenceKey()))
                                .map(x -> x.getTimeIntervals())
                                .iterator().next().iterator().next()
                                .getFrom().equals(DateFormat.getDateInstance().parse("12.08.2018"))) {
                            System.err.println("DEBUG");
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    */
                    
                    //////////////////////////////////////////////////////////////
                    
                    if(mapped) {
                        personToNumMap.put(person, personToNumMap.get(person) + 1);
                        alreadyMapped.add(person);
                        //alreadyTried.clear(); //TODO: eigentlich nicht mehr benoetigt, da alternation das ueberprueft!
                        //double task-instance mapping to one person not possible atm with this solver!
                        //alreadyTried.addAll(alreadyMapped);
                    }/* else {*/
                    alreadyTried.add(person);
                    /*}*/
                    
                    if(mapped && task.getNumberOfInstances() == 0) {
                        taskMapped = true;
                    }
                    
                    person = getNextPerson(personToNumMap, alreadyTried);
                }
                
                if(!taskMapped && !this.computePartialSolutionWhenSolvingNotPossible) {
                    break;
                }
            }   
            
            System.out.println(personToNumMap); //TODO: DEBUG-Ausgabe entfernen!
            
            boolean fullMapped = world.isCompletelyMapped();
            return fullMapped;
        }
        throw new IllegalStateException("World was not set!");
    }

    private Person getNextPerson(final Map<Person, Integer> map, final Set<Person> alreadyTried) {
        //TODO: optimize
        int min = Integer.MAX_VALUE;
        Person pMin = null;
        for(Entry<Person, Integer> entry : map.entrySet()) {
            final Person person = entry.getKey();
            final int num = entry.getValue();
            if (!alreadyTried.contains(person) && num <= min) {
                min = num;
                pMin = person;
            }
        }
        
        return pMin;
    }
}
