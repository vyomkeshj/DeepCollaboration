package edu.vsb.realCollaborationn.visualization.utils3d.geom.transform;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Affine3DTest {

    @Test
    void testNativeTransformation() {
        Affine3D initialAffine = new Affine3D();
        initialAffine.translate(0.6,2,1.4);

        Affine3D rotationAffine = new Affine3D();
        rotationAffine.setToRotation(90, 0, 0, 1);

        System.out.println("translation affine= "+initialAffine);
        System.out.println("rotation affine= "+rotationAffine);
        initialAffine.preTransform(rotationAffine);

        System.out.println("pretransformation result"+initialAffine);
    }
}