package base.solution;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import base.Person;

public class OptimizedFairNumSolver extends FairNumSolver {
    private int randomizeLvl;
    
    public OptimizedFairNumSolver() {
        super();
        this.randomizeLvl = 0;
    }
    
    public OptimizedFairNumSolver(final boolean computePartialSolutionWhenSolvingNotPossible, final int randomizeLvl) {
        super(computePartialSolutionWhenSolvingNotPossible);
        if(randomizeLvl < 0) throw new IllegalArgumentException("randomizeLvl must be >= 0");
        this.randomizeLvl = randomizeLvl;
    }

    @Override
    protected Person getNextPerson(final Map<Person, Integer> map, final Set<Person> alreadyTried) {
        //TODO: evtl. optimization of sorting (but probably this is not possible, because initial sorting
        //would require O(n log n) > O(n) of linear search :(
        int min = Integer.MAX_VALUE;
        List<Person> pMin = new ArrayList<>(map.size() + 1);
        
        int randomizedDec = this.randomizeLvl;   
        
        for(Entry<Person, Integer> entry : map.entrySet()) {
            final Person person = entry.getKey();
            final int num = entry.getValue();
            if (!alreadyTried.contains(person) && num <= min) {
                if(num < min) {
                    min = num;
                    pMin.clear();
                }
                pMin.add(person);
                if(randomizedDec > 0) {
                    Collections.shuffle(pMin);
                    randomizedDec--;
                }
            }
        }
        for(int i = 0;i < randomizedDec;i++) {
            Collections.shuffle(pMin);
        }
        return pMin.isEmpty() ? null : pMin.get(0);
    }
}
