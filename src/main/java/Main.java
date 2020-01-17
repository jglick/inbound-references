import java.io.File;
import java.util.List;
import java.util.Random;
import org.gridkit.jvmtool.heapdump.InboundAnalyzer;
import org.netbeans.lib.profiler.heap.Heap;
import org.netbeans.lib.profiler.heap.HeapFactory;
import org.netbeans.lib.profiler.heap.Instance;
import org.netbeans.lib.profiler.heap.JavaClass;

public class Main {
    public static void main(String[] args) throws Exception {
        File dump = new File(args[0]);
        String clazz = args[1];
        System.err.print("Loading " + dump + "…");
        Heap heap = HeapFactory.createFastHeap(dump);
        System.err.println("done");
        System.err.print("Looking for " + clazz + "…");
        JavaClass jc = heap.getJavaClassByName(clazz);
        List<Instance> instances = jc.getInstances();
        int size = instances.size();
        System.err.println("found " + size);
        System.err.print("Initializing inbound analysis…");
        InboundAnalyzer ia = new InboundAnalyzer(heap);
        ia.initRoots();
        System.err.println("done");
        while (true) {
            int x = new Random().nextInt(size);
            System.err.print("Looking at #" + x + "…");
            Instance i = instances.get(x);
            ia.mark(i.getInstanceId());
            int d = 1;
            while (!ia.isExhausted()) {
                System.out.println("\nDepth " + d);
                ia.report();
                ++d;
            }
            System.err.println();
        }
    }
}
