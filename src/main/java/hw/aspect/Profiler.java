package hw.aspect;

import hw.aspect.meta.MethodInfo;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Aspect
@Component
public class Profiler {
    public static final List<MethodInfo> methods = new ArrayList<>();

    @Around("@annotation(Profile)")
    public Object profileMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        LocalDateTime startTime = LocalDateTime.now();
        Object result = joinPoint.proceed(joinPoint.getArgs());
        LocalDateTime endTime = LocalDateTime.now();

        String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        String args = Arrays.stream(joinPoint.getArgs()).map(Object::toString).collect(Collectors.joining(", "));
        long msBetween = ChronoUnit.MILLIS.between(startTime, endTime);

        String currentMethod = className + "." + methodName + "(" + args + ") elapsed: " + msBetween + "'ms";
        String format = "[PROF--LOG] ";
        System.out.println(format + currentMethod + System.lineSeparator());
        methods.add(new MethodInfo(methodName, msBetween));
        return result;
    }

    public static void statistics() {
        String format = """
                  ___ ___  ___  ___ ___ _    ___ ___\s
                 | _ | _ \\/ _ \\| __|_ _| |  | __| _ \\
                 |  _|   | (_) | _| | || |__| _||   /
                 |_| |_|_\\\\___/|_| |___|____|___|_|_\\
                                                     \
                """;
        System.out.println(format);

        List<MethodInfo> methodsCalls = Profiler.methods;
        // count calls of each method
        methodsCalls.stream()
                .collect(
                        Collectors.groupingBy(
                                MethodInfo::name,
                                Collectors.counting()
                        )
                )
                .forEach((methodName, count) -> System.out.println("[PROF-STAT] " + methodName + " called " + count + "'times"));

        // count average execution time of each method
        methodsCalls.stream()
                .collect(
                        Collectors.groupingBy(
                                MethodInfo::name,
                                Collectors.averagingLong(MethodInfo::time)
                        )
                )
                .forEach((methodName, avgTime) -> System.out.println("[PROF-STAT] " + methodName + " average execution time is " + avgTime + "'ms"));

        // count total execution time of each method
        methodsCalls.stream()
                .collect(
                        Collectors.groupingBy(
                                MethodInfo::name,
                                Collectors.summingLong(MethodInfo::time)
                        )
                )
                .forEach((methodName, totalTime) -> System.out.println("[PROF-STAT] " + methodName + " total execution time is " + totalTime + "'ms"));
    }
}
