package com.cognitio.herbology.client;

import net.minecraft.client.resources.model.BakedModel;
import java.lang.reflect.Method;
import java.io.FileWriter;
import java.io.PrintWriter;

public class ReflectModel {
    public static void main(String[] args) throws Exception {
        PrintWriter pw = new PrintWriter(new FileWriter("methods.txt"));
        for (Method m : BakedModel.class.getMethods()) {
            pw.println(m.getReturnType().getName() + " " + m.getName());
            for (Class<?> p : m.getParameterTypes()) {
                pw.println("  " + p.getName());
            }
        }
        pw.close();
    }
}
