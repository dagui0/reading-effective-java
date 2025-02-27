import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

@Disabled
public class SystemPropertyTest {

    private String captureExternalOutput(String[] cmdline) throws IOException {
        Process p = Runtime.getRuntime().exec(cmdline);
        String s = new String(p.getInputStream().readAllBytes());
        return s.replaceAll("\r?\n", "");
    }

    @Test
    public void testPrintJavaSystemProperties() throws IOException {

        String osName = System.getProperty("os.name");
        String osArch = System.getProperty("os.arch");

        System.out.println("os.name: " + osName);
        System.out.println("os.arch: " + osArch);

        if (osName.startsWith("Windows")) {
            Map<String, String> env = System.getenv();
            String OS = env.get("OS");
            String PROCESSOR_ARCHITECTURE = env.get("PROCESSOR_ARCHITECTURE");
            System.out.println("%OS%: " + OS);
            System.out.println("%PROCESSOR_ARCHITECTURE%: " + PROCESSOR_ARCHITECTURE);
        }
        else {
            String uname_s = captureExternalOutput(new String[] {"/usr/bin/uname", "-s"});
            String uname_m = captureExternalOutput(new String[] {"/usr/bin/uname", "-m"});
            System.out.println("$(uname -s): " + uname_s);
            System.out.println("$(uname -m): " + uname_m);
        }
        // System.getProperties().list(System.out);
    }
}

/*

java.specification.version=21
sun.cpu.isalist=amd64
sun.jnu.encoding=MS949
org.gradle.test.worker=2
java.class.path=C:\\Users\\dagui\\.gradle\\caches\\8....
java.vm.vendor=Eclipse Adoptium
sun.arch.data.model=64
user.variant=
java.vendor.url=https://adoptium.net/
user.timezone=Asia/Seoul
java.vm.specification.version=21
os.name=Windows 11
user.country=KR
sun.java.launcher=SUN_STANDARD
sun.boot.library.path=C:\Users\dagui\.gradle\jdks\eclipse_a...
sun.java.command=worker.org.gradle.process.internal.wo...
jdk.debug=release
sun.cpu.endian=little
user.home=C:\Users\dagui
user.language=ko
java.specification.vendor=Oracle Corporation
java.version.date=2025-01-21
java.home=C:\Users\dagui\.gradle\jdks\eclipse_a...
file.separator=\
java.vm.compressedOopsMode=32-bit
line.separator=

java.vm.specification.vendor=Oracle Corporation
java.specification.name=Java Platform API Specification
user.script=
sun.management.compiler=HotSpot 64-Bit Tiered Compilers
java.runtime.version=21.0.6+7-LTS
user.name=dagui
stdout.encoding=MS949
path.separator=;
os.version=10.0
java.runtime.name=OpenJDK Runtime Environment
file.encoding=UTF-8
java.vm.name=OpenJDK 64-Bit Server VM
java.vendor.version=Temurin-21.0.6+7
java.vendor.url.bug=https://github.com/adoptium/adoptium-...
java.io.tmpdir=C:\Users\dagui\AppData\Local\Temp\
java.version=21.0.6
user.dir=C:\Users\dagui\src\dagui0\reading-eff...
os.arch=amd64
java.vm.specification.name=Java Virtual Machine Specification
org.gradle.internal.worker.tmpdir=C:\Users\dagui\src\dagui0\reading-eff...
sun.os.patch.level=
native.encoding=MS949
java.library.path=C:\Users\dagui\src\dagui0\reading-eff...
java.vm.info=mixed mode, sharing
stderr.encoding=MS949
java.vendor=Eclipse Adoptium
java.vm.version=21.0.6+7-LTS
sun.io.unicode.encoding=UnicodeLittle
java.class.version=65.0

 */
