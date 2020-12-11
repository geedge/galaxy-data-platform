package com.mesalab.tool.utils;/**
 * Created by dell on 2018-10-8.
 */

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.maxmind.db.*;
import com.maxmind.geoip2.exception.AddressNotFoundException;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.*;
import com.mesalab.tool.domain.LocationResponse;
import com.mesalab.tool.domain.JsonInjector;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.Collections;
import java.util.List;

/**
 * @ClassName GalaxyDataBaseReader
 * @Description TODO
 * @Author dell
 * @Date 2018-10-8 18:30
 * @Version 1.0
 **/
public class GalaxyDataBaseReader  {

    private final Reader reader;

    private final ObjectMapper om;

    private final List<String> locales;

    private GalaxyDataBaseReader(Builder builder) throws IOException {
        if (builder.stream != null) {
            this.reader = new Reader(builder.stream, builder.cache);
        } else if (builder.database != null) {
            this.reader = new Reader(builder.database, builder.mode, builder.cache);
        } else {
            // This should never happen. If it does, review the Builder class
            // constructors for errors.
            throw new IllegalArgumentException(
                    "Unsupported Builder configuration: expected either File or URL");
        }
        this.om = new ObjectMapper();
        this.om.configure(MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS, false);
        this.om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                false);
        this.om.configure(
                DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
        this.locales = builder.locales;
    }

    /**
     * <p>
     * Constructs a Builder for the {@code DatabaseReader}. The file passed to
     * it must be a valid GeoIP2 database file.
     * </p>
     * <p>
     * {@code Builder} creates instances of {@code DatabaseReader}
     * from values set by the methods.
     * </p>
     * <p>
     * Only the values set in the {@code Builder} constructor are required.
     * </p>
     */
    public static final class Builder {
        final File database;
        final InputStream stream;

        List<String> locales = Collections.singletonList("en");
        Reader.FileMode mode = Reader.FileMode.MEMORY_MAPPED;
        NodeCache cache = NoCache.getInstance();

        /**
         * @param stream the stream containing the GeoIP2 database to use.
         */
        public Builder(InputStream stream) {
            this.stream = stream;
            this.database = null;
        }

        /**
         * @param database the GeoIP2 database file to use.
         */
        public Builder(File database) {
            this.database = database;
            this.stream = null;
        }

        /**
         * @param val List of locale codes to use in name property from most
         *            preferred to least preferred.
         * @return Builder object
         */
        public Builder locales(List<String> val) {
            this.locales = val;
            return this;
        }

        /**
         * @param cache backing cache instance
         * @return Builder object
         */
        public Builder withCache(NodeCache cache) {
            this.cache = cache;
            return this;
        }

        /**
         * @param val The file mode used to open the GeoIP2 database
         * @return Builder object
         * @throws java.lang.IllegalArgumentException if you initialized the Builder with a URL, which uses
         *                                            {@link Reader.FileMode#MEMORY}, but you provided a different
         *                                            FileMode to this method.
         */
        public Builder fileMode(Reader.FileMode val) {
            if (this.stream != null && Reader.FileMode.MEMORY != val) {
                throw new IllegalArgumentException(
                        "Only FileMode.MEMORY is supported when using an InputStream.");
            }
            this.mode = val;
            return this;
        }

        /**
         * @return an instance of {@code DatabaseReader} created from the
         * fields set on this builder.
         * @throws IOException if there is an error reading the database
         */
        public GalaxyDataBaseReader build() throws IOException {
            return new GalaxyDataBaseReader(this);
        }
    }


    /**
     * @param ipAddress IPv4 or IPv6 address to lookup.
     * @return A <T> object with the data for the IP address
     * @throws IOException              if there is an error opening or reading from the file.
     * @throws AddressNotFoundException if the IP address is not in our database
     */
    private <T> T get(InetAddress ipAddress, Class<T> cls,
                      String type) throws IOException, AddressNotFoundException {

        String databaseType = this.getMetadata().getDatabaseType();
        if (!databaseType.contains(type)) {
            String caller = Thread.currentThread().getStackTrace()[2]
                    .getMethodName();
            throw new UnsupportedOperationException(
                    "Invalid attempt to open a " + databaseType
                            + " database using the " + caller + " method");
        }

        ObjectNode node = jsonNodeToObjectNode(reader.get(ipAddress));

        // We throw the same exception as the web service when an IP is not in
        // the database
        if (node == null) {
            throw new AddressNotFoundException("The address "
                    + ipAddress.getHostAddress() + " is not in the database.");
        }

        InjectableValues inject = new JsonInjector(locales, ipAddress.getHostAddress());
        return this.om.reader(inject).treeToValue(node, cls);
    }

    private ObjectNode jsonNodeToObjectNode(JsonNode node)
            throws InvalidDatabaseException {
        if (node == null || node instanceof ObjectNode) {
            return (ObjectNode) node;
        }
        throw new InvalidDatabaseException(
                "Unexpected data type returned. The GeoIP2 database may be corrupt.");
    }



    public LocationResponse location(InetAddress ipAddress) throws IOException, GeoIp2Exception {
        return this.get(ipAddress, LocationResponse.class, "");

    }


    public void close() throws IOException {
        this.reader.close();
    }


    /**
     * @return the metadata for the open MaxMind DB file.
     */
    public Metadata getMetadata() {
        return this.reader.getMetadata();
    }
}
