package ru.alastar.game;

import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.physics.bullet.softbody.btSoftBody;

/**
 * Created by mick on 15.06.15.
 */
public class GMaterial {
    //Engine representation of object material
    //Includes texture, tiling, attrs, physics attrs

    private com.badlogic.gdx.graphics.g3d.Material _gdxMat;
    private float _friction = 0.0f;
    private float _density = 0.0f;
    private float _restitution = 0.0f;
    private com.badlogic.gdx.physics.bullet.softbody.btSoftBody.Material _softMat;
    private int _xTiling = 1;
    private int _yTiling = 1;

    public GMaterial()
    {

    }

    public Material get_gdxMat() {
        return _gdxMat;
    }

    public void set_gdxMat(Material _gdxMat) {
        this._gdxMat = _gdxMat;
    }

    public float get_friction() {
        return _friction;
    }

    public void set_friction(float _friction) {
        this._friction = _friction;
    }

    public float get_density() {
        return _density;
    }

    public void set_density(float _density) {
        this._density = _density;
    }

    public float get_restitution() {
        return _restitution;
    }

    public void set_restitution(float _restitution) {
        this._restitution = _restitution;
    }

    public btSoftBody.Material get_softMat() {
        return _softMat;
    }

    public void set_softMat(btSoftBody.Material _softMat) {
        this._softMat = _softMat;
    }

    public int get_xTiling() {
        return _xTiling;
    }

    public void set_xTiling(int _xTiling) {
        this._xTiling = _xTiling;
    }

    public int get_yTiling() {
        return _yTiling;
    }

    public void set_yTiling(int _yTiling) {
        this._yTiling = _yTiling;
    }
}
