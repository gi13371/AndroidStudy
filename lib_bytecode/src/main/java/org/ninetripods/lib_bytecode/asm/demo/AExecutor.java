package org.ninetripods.lib_bytecode.asm.demo;

import static org.ninetripods.lib_bytecode.BConstantKt.log;
import static org.ninetripods.lib_bytecode.asm.demo.ATimeCostClassVisitorKt.FIELD_NAME_ADD;

import org.ninetripods.lib_bytecode.BConstant;
import org.ninetripods.lib_bytecode.util.Loader;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.io.IOException;
import java.util.Arrays;

public class AExecutor {

    public static void main(String[] args) {
        try {
            ClassReader classReader = new ClassReader(MethodTimeCostTest.class.getName());
            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            ClassVisitor classVisitor = new ATimeCostClassVisitor(BConstant.ASM9, classWriter);
            classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES);

            Loader loader = new Loader();
            try {
                log("classWriter ByteArray: " + Arrays.toString(classWriter.toByteArray()));
                Class addTimeClass = loader.defineClass(MethodTimeCostTest.class.getName(), classWriter.toByteArray());
                Object instance = addTimeClass.newInstance();
                addTimeClass.getDeclaredMethod("addTimeCostMonitor").invoke(instance);
                Long timeCost = addTimeClass.getDeclaredField(FIELD_NAME_ADD).getLong(instance);
                log("timeCost:" + timeCost);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}