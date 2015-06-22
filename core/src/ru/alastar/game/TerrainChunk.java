package ru.alastar.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import ru.alastar.Engine;

class TerrainChunk {
    private final float[] heightMap;
    public final short width;
    public final short height;
    public float[] vertices;
    public short[] indices;
    private static int vertexSize;
    private static final int positionSize = 3;
    private final static Vector3 vec = new Vector3();
    private final static Vector3 tmpV = new Vector3();

    public TerrainChunk(int width, int vertexSize, float[][] map) {
        if ((32 + 1) * (32 + 1) > Short.MAX_VALUE)
            throw new IllegalArgumentException(
                    "Chunk size too big, (width + 1)*(height+1) must be <= 32767");

        this.heightMap = new float[(32 + 1) * (32 + 1)];
        this.width = (short) 32;
        this.height = (short) 32;
        this.vertices = new float[heightMap.length * Engine.vertexSize];
        this.indices = new short[32 * 32 * 6];
        TerrainChunk.vertexSize = Engine.vertexSize;

        buildHeightmap(map);
        buildIndices();
        buildVertices();
        calcNormals(indices, vertices);
        // EditorScreen.Log("", "" + vertices.length + " " + indices.length);
    }

    public TerrainChunk(int width, int height, int vertexSize, float[] verts,
                        short[] indices) {
        this.heightMap = new float[(width + 1) * (height + 1)];
        this.width = (short) width;
        this.height = (short) height;
        TerrainChunk.vertexSize = vertexSize;
        this.vertices = verts;
        this.indices = indices;
        calcNormals(indices, vertices);
    }

    void buildHeightmap(float[][] map) {

        int idh = 0; // index to iterate

        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {

                this.heightMap[idh++] = map[x][y];
            }
        }
    }

    void buildVertices() {

        int idx = 0;
        int hIdx = 0;

        for (int z = 0; z < height + 1; z++) {
            for (int x = 0; x < width + 1; x++) {

                // POSITION
                vertices[idx++] = z;
                vertices[idx++] = heightMap[hIdx++];
                vertices[idx++] = x;

                // NORMALS
                vertices[idx++] = 0;
                vertices[idx++] = 1;
                vertices[idx++] = 0;

                // COLOR
                vertices[idx++] = Color.WHITE.toFloatBits();

                // TEXTURE
                vertices[idx++] = (x / (float) width);
                vertices[idx++] = (z / (float) height);

            }
        }
    }

    private void buildIndices() {
        int idx = 0;
        short pitch = (short) (width + 1);
        short i1 = 0;
        short i2 = 1;
        short i3 = (short) (1 + pitch);
        short i4 = pitch;

        short row = 0;

        for (int z = 0; z < height; z++) {
            for (int x = 0; x < width; x++) {
                indices[idx++] = i1;
                indices[idx++] = i2; // i3 is exchanged
                indices[idx++] = i3; // with i2

                indices[idx++] = i3;
                indices[idx++] = i4; // i1 is exchanged
                indices[idx++] = i1; // with i4

                i1++;
                i2++;
                i3++;
                i4++;
            }

            row += pitch;
            i1 = row;
            i2 = (short) (row + 1);
            i3 = (short) (i2 + pitch);
            i4 = (short) (row + pitch);
        }
    }

    // Gets the index of the first float of a normal for a specific vertex
    private static int getNormalStart(int vertIndex) {
        return vertIndex * vertexSize + positionSize;
    }

    // Gets the index of the first float of a specific vertex
    private static int getPositionStart(int vertIndex) {
        return vertIndex * vertexSize;
    }

    // Adds the provided value to the normal
    private static void addNormal(int vertIndex, float[] verts, float x,
                                  float y, float z) {

        int i = getNormalStart(vertIndex);

        verts[i] += x;
        verts[i + 1] += y;
        verts[i + 2] += z;
    }

    /*
     * Normalizes normals
     */
    private static void normalizeNormal(int vertIndex, float[] verts) {

        final int i = getNormalStart(vertIndex);

        float x = verts[i];
        float y = verts[i + 1];
        float z = verts[i + 2];

        float num2 = ((x * x) + (y * y)) + (z * z);
        float num = 1f / (float) Math.sqrt(num2);
        x *= num;
        y *= num;
        z *= num;
        vec.set(x, y, z);
        vec.rotate(tmpV.set(0, 1, 0), 180);
        // EditorScreen.Log("NORMALS", vec.toString());
        verts[i] = vec.x;
        verts[i + 1] = vec.y;
        verts[i + 2] = vec.z;
    }

    /*
     * Calculates the normals
     */
    public static void calcNormals(short[] indices, float[] verts) {
        int i1;
        int i2;
        int i3;
        float x1, num, num2, y1, z1, x2, y2, z2, x3, y3, z3, ux, uy, uz, nx, ny, nz, vx, vy, vz;
        for (int i = 0; i < indices.length; i += 3) {
            i1 = getPositionStart(indices[i]);
            i2 = getPositionStart(indices[i + 1]);
            i3 = getPositionStart(indices[i + 2]);

            // p1
            x1 = verts[i1];
            y1 = verts[i1 + 1];
            z1 = verts[i1 + 2];

            // p2
            x2 = verts[i2];
            y2 = verts[i2 + 1];
            z2 = verts[i2 + 2];

            // p3
            x3 = verts[i3];
            y3 = verts[i3 + 1];
            z3 = verts[i3 + 2];

            // u = p3 - p1
            ux = x3 - x1;
            uy = y3 - y1;
            uz = z3 - z1;

            // v = p2 - p1
            vx = x2 - x1;
            vy = y2 - y1;
            vz = z2 - z1;

            // n = cross(v, u)
            nx = (vy * uz) - (vz * uy);
            ny = (vz * ux) - (vx * uz);
            nz = (vx * uy) - (vy * ux);

            // normalize(n)
            num2 = ((nx * nx) + (ny * ny)) + (nz * nz);
            num = 1f / (float) Math.sqrt(num2);
            nx *= num;
            ny *= num;
            nz *= num;

            addNormal(indices[i], verts, nx, ny, nz);
            addNormal(indices[i + 1], verts, nx, ny, nz);
            addNormal(indices[i + 2], verts, nx, ny, nz);
        }

        for (int i = 0; i < (verts.length / vertexSize); i++) {
            normalizeNormal(i, verts);
        }
    }

}
