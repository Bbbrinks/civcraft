package nl.civcraft.opengl.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Created by Bob on 29-9-2017.
 * <p>
 * This is probably not worth documenting
 */
public class Slf4jPrintStream extends PrintStream {

    public Slf4jPrintStream(Logger logger,
                            Level level){
        super(new OutputStream() {

            private String mem;

            /**
             * Writes a byte to the output stream. This method flushes automatically at the end of a line.
             *
             * @param b DOCUMENT ME!
             */
            public void write (int b) {
                byte[] bytes = new byte[1];
                bytes[0] = (byte) (b & 0xff);
                mem = mem + new String(bytes);

                if (mem.endsWith ("\n")) {
                    mem = mem.substring (0, mem.length () - 1);
                    flush ();
                }
            }

            /**
             * Flushes the output stream.
             */
            public void flush () {
                logger.log (level, mem);
                mem = "";
            }
        });
    }
}
