public class ByteAClassWriter extends ClassLoader {

    static{
        registerAsParallelCapable();
    }
    public static final ByteAClassWriter INSTANCE = new ByteAClassWriter();
    public Class<?> defineClass(String binaryName, byte[] bytecode){
        return defineClass(binaryName, bytecode,0, bytecode.length);
    }

}
