package com.ae.custom{
    
    /**
     * @author Arnaud FOUCAL
     * 
     * @version 1.0
     * @Comment  1st version
     * 
    */
    
    import flash.display.Graphics;
    import flash.geom.Matrix;
    
    import mx.containers.Canvas;
    import mx.styles.CSSStyleDeclaration;
    import mx.styles.StyleManager;
    
    [Exclude(name = "backgroundColor", kind = "style")]
    [Exclude(name = "borderColor", kind = "style")]
    [Exclude(name = "fillAlphas", kind = "style")]
    
    /**
     * An array of all colors defining the gradient.
     * 
     * @default 0xFFFFFF 
     */
    [Style(name = "fillColors", type = "Array", format = "Color", inherit = "no")]
    
    /**
     * An array defining the alpha of each color in the gradient. Values must be between 0 and 1.
     * You must have the same number of alphas values than the number of colors in the gradient.
     * 
     * @default 1
     */
    [Style(name = "fillAlphas", type = "Array", format = "Number", inherit = "no")]
    
    /**
     * Defines the type of gradient. The gradient can be "linear" or "radial".
     * 
     * @ default "linear"
     */
    [Style(name = "gradientType", type = "String", format = "String", enumeration = "linear, radial", inherit = "no")]
    
    /**
     * An array of color distribution ratios; valid values are 0 to 255. This value defines the percentage of the width where the color is sampled at 100%. The value 0 represents the left-hand position in the gradient box, and 255 represents the right-hand position in the gradient box.
    *
    * <b>Note</b>: This value represents positions in the gradient box, not the coordinate space of the final gradient, which might be wider or thinner than the gradient box. Specify a value for each value in the colors parameter.
     * 
     * The values in the array must increase sequentially; for example, [0, 63, 127, 190, 255]. 
     */
    [Style(name = "gradientRatio", type = "Array", format = "Number", inherit = "no")]
    
    /**
     * The value from the SpreadMethod class that specifies which spread method to use for the gradient.
     * Possible vaues are : "pad", "reflect", "repeat".
     * 
     * @default "pad"
     */
    [Style(name = "spreadMethod", type = "String", format = "String", enumeration = "pad, reflect, repeat", inherit = "no")]
    
    /**
     * The value from the InterpolationMethod class that specifies which value to use for gradient.
     * Possible values are : "linearRGB" or "rgb".
     * 
     * @default "rgb"
     */
    [Style(name = "interpolationMethod", type = "String", format = "String", enumeration = "rgb, linearRGB", inherit = "no")]
    
    /**
     * The number that controls the location of the focal point of the gradient.
     * 0 means that the focal point is in the center.
     * 1 means that the focal point is at one border of the gradient circle.
     * -1 means that the focal point is at the other border of the gradient circle.
     * A value less than -1 or greater than 1 is rounded to -1 or 1.
     * 
     * @default 0
     */
    [Style(name = "focalPointRatio", type = "Number", format = "Number", inherit = "no")]
    
    /**
     * Defines the rotation of the gradient., in degree.
     * This style has no visual result with a radial gradient excepted if the focal point ratio is different than 0.
     * 
     * @default 0
     */
    [Style(name = "angle", type = "Number", format = "Number", inherit = "no")]
    
    /**
     * How far the gradient is horizontally offset from the origin.
     * 
     * @default 0
     */
    [Style(name = "offsetX", type = "Number", format = "Number", inherit = "no")]
    
    /**
     * How far the gradient is vertically offset from the origin.
     * 
     * @default 0
     */
    [Style(name = "offsetY", type = "Number", format = "Number", inherit = "no")]
    
    /**
     * Radius 
     */
    [Style(name = "cornerRadius", type = "Number", format = "Number", inherit = "no")]
    
    /**
     * Radius of the upper-left corner of the canvas, in pixels
     * 
     * @default 0
     */
    [Style(name = "topLeftRadius", type = "Number", format = "Number", inherit = "no")]
    
    /**
     * Radius of the upper-right corner of the canvas, in pixels.
     * 
     * @default 0
     */
    [Style(name = "topRightRadius", type = "Number", format = "Number", inherit = "no")]
    
    /**
     * Radius of the bottom-left corner of the canvas, in pixels.
     * 
     * @default 0
     */
    [Style(name = "bottomLeftRadius", type = "Number", format = "Number", inherit = "no")]
    
    /**
     * Radius of the bottom-right corner of the canvas, in pixels.
     * 
     * @default 0
     */
    [Style(name = "bottomRightRadius", type = "Number", format = "Number", inherit = "no")]
    
    /**
     * <p>The GradientCanvas extends the class mx:Canvas by adding capabilities of styling the background.</p>
     * <p>The GradientCanvas has a gradient background that can be totally customized.
     * The corners of the canvas can be rounded individually.</p>
     *    
     *  @mxml
     *
     *  <p>The <code>&lt;GradientCanvas&gt;</code> tag inherits all the tag attributes of its superclass. Use the following syntax:</p>
     *  <pre>
     *  &lt;com:GradientCanvas&gt;
     *    ...
     *      <i>child tags</i>
     *    ...
     *  &lt;/GradientCanvas&gt;
     *  </pre>
     * 
     *  @see http://livedocs.adobe.com/flex/3/langref/flash/display/Graphics.html#beginGradientFill() beginGradientFill
     */
    public class GradientCanvas extends Canvas
    {
        private var _fillColors : Array;
        private var _fillAlphas : Array;
        private var _angle : Number;
        private var _gradientType : String;
        private var _gradientRatio : Array;
        private var _spreadMethod : String;
        private var _interpolationMethod : String;
        private var _focalPointRatio : Number;
        private var _offsetx : Number;
        private var _offsety : Number;
        private var _topLeftRadius : Number;
        private var _topRightRadius : Number;
        private var _bottomLeftRadius : Number;
        private var _bottomRightRadius : Number;
        
        // Default value of  GradientCanvas styles
        public static const DEFAULT_GRADIENTTYPE : String = "linear";
        public static const DEFAULT_FILLCOLORS : String = "0xFFFFFF";
        public static const DEFAULT_FILLALPHAS : Number = 1;
        public static const DEFAULT_ANGLE : Number = 0;
        public static const DEFAULT_GRADIENTRATIO : Number = 255;
        public static const DEFAULT_OFFSETX : Number = 0;
        public static const DEFAULT_OFFSETY : Number = 0;
        public static const DEFAULT_SPREADMETHOD : String = "pad";
        public static const DEFAULT_INTERPOLATIONMETHOD : String = "rgb";
        public static const DEFAULT_FOCALPOINTRATIO : Number = 0;
        public static const DEFAULT_CORNERRADIUS : Number = 0;
        public static const DEFAULT_TOPLEFTRADIUS : Number = 0;
        public static const DEFAULT_TOPRIGHTRADIUS : Number = 0;
        public static const DEFAULT_BOTTOMLEFTRADIUS : Number = 0;
        public static const DEFAULT_BOTTOMRIGHTRADIUS : Number = 0;
        
        // Define a static variable.
        private static var classConstructed:Boolean = classConstruct();
    
        /**
        * @private Define a static method.
        */
        private static function classConstruct() :Boolean
        {
            if (!StyleManager.getStyleDeclaration ("GradientCanvas"))
           {
                // If there is no CSS definition for GradientCanvas, 
                // then create one and set the default value.
                var myCSS:CSSStyleDeclaration = new CSSStyleDeclaration();
                
                myCSS.defaultFactory = function() :void
                {
                    this._fillColors = new Array(DEFAULT_FILLCOLORS);
                    this._fillAlphas = new Array(DEFAULT_FILLALPHAS.toString());
                    this._angle = DEFAULT_ANGLE;
                    this._gradientType = DEFAULT_GRADIENTTYPE;
                    this._gradientRatio = new Array(DEFAULT_GRADIENTRATIO.toString());
                    this._offsetX = DEFAULT_OFFSETX;
                    this._offsetY = DEFAULT_OFFSETY;
                    this._spreadMethod = DEFAULT_SPREADMETHOD;
                    this._interpolationMethod = DEFAULT_INTERPOLATIONMETHOD;
                    this._focalPointRatio = DEFAULT_FOCALPOINTRATIO;
                    this._cornerRadius = DEFAULT_CORNERRADIUS;
                    this._topLeftRadius = DEFAULT_TOPLEFTRADIUS;
                    this._topRightRadius = DEFAULT_TOPRIGHTRADIUS;
                    this._bottomLeftRadius = DEFAULT_BOTTOMLEFTRADIUS;
                    this._bottomRightRadius = DEFAULT_BOTTOMRIGHTRADIUS;
                }
                StyleManager.setStyleDeclaration ("GradientCanvas", myCSS, true);
            }
            return true;
        }
        
        /**
         * Constructor
         */
        public function GradientCanvas()
        {
            super();
            
            // Set style of fillAlphas to prevent unwanted inheritance of that style
            setStyle("fillAlphas", new Array(DEFAULT_FILLALPHAS.toString()));
        }
        
        /**
         * Overrive the function of the super Class and add the following:
         * <ul><li> defines default values of the new styles,</li>
         * <li> draw the background defined by customized styles.</li></ul>
         * 
         * @param w:Number The width of the GradientCanvas.
         * @param h:Number The height of the GradientCanvas.
         */
        override protected function updateDisplayList (w : Number, h : Number) :void
        {
            super.updateDisplayList (w, h);
            
            // Check if styles have changed
            if (bStylePropChanged==true) 
            {
                /*
                * Retrieves the user-defined styles or assign default values
                */
                if (getStyle('gradientType')!=null)            { _gradientType = getStyle ('gradientType');}            else { _gradientType = DEFAULT_GRADIENTTYPE; }
                if (getStyle ('fillColors') != null)        { _fillColors = getStyle ('fillColors'); }        else { _fillColors = new Array(DEFAULT_FILLCOLORS);}

                if (getStyle('fillAlphas') != 1)        { _fillAlphas = getStyle ('fillAlphas'); }
                    // build the default value array of alphas according to the number of colors
                    else {
                        _fillAlphas = new Array();
                        for (var i:Number = 0; i < _fillColors.length; i++) {
                            _fillAlphas.push (DEFAULT_FILLALPHAS.toString());}
                    }
                
                if (getStyle ('gradientRatio') != null)        { _gradientRatio = getStyle ('gradientRatio'); }
                    // build the default value array of ratio according to the number of colors
                    else {
                        _gradientRatio = new Array();
                        for (var j:Number = 0; j < _fillColors.length; j++) {
                            _gradientRatio = generateDefaultRatio(_fillColors.length);}
                    }
                    
                if (getStyle('angle') != null)        { _angle = getStyle('angle'); }        else { _angle = DEFAULT_ANGLE; }
                if (getStyle ('spreadMethod') != null)        { _spreadMethod = getStyle ('spreadMethod'); }        else { _spreadMethod = DEFAULT_SPREADMETHOD; }
                if (getStyle ('interpolationMethod') != null)        { _interpolationMethod = getStyle ('interpolationMethod');}        else { _interpolationMethod = DEFAULT_INTERPOLATIONMETHOD; }
                if (getStyle ('focalPointRatio') != null)        { _focalPointRatio = getStyle ('focalPointRatio'); }        else { _focalPointRatio = DEFAULT_FOCALPOINTRATIO; }
                if (getStyle ('offsetX') != null)        { _offsetx = getStyle ('offsetX'); }        else { _offsetx = DEFAULT_OFFSETX; }
                if (getStyle ('offsetY') != null)        { _offsety = getStyle ('offsetY');}        else { _offsety = DEFAULT_OFFSETY; }
                
                // Check if at least 1 individual corner has style and then use each corner as style
                if ( (getStyle ('topLeftRadius') != 0 && getStyle ('topLeftRadius') != null) ||
                (getStyle ('topRightRadius') != 0 && getStyle ('topRightRadius') != null) ||
                (getStyle ('bottomLeftRadius') != 0 && getStyle ('bottomLeftRadius') != null) ||
                (getStyle ('bottomRightRadius') != 0 && getStyle ('bottomRightRadius') != null))
                { 
                    if (getStyle ('topLeftRadius') != null)        { _topLeftRadius = getStyle ('topLeftRadius'); }        else { _topLeftRadius = DEFAULT_TOPLEFTRADIUS; }
                    if (getStyle ('topRightRadius') != null)        { _topRightRadius = getStyle ('topRightRadius'); }        else { _topRightRadius = DEFAULT_TOPRIGHTRADIUS; }
                    if (getStyle ('bottomLeftRadius') != null)        { _bottomLeftRadius = getStyle ('bottomLeftRadius'); }        else { _bottomLeftRadius = DEFAULT_BOTTOMLEFTRADIUS; }
                    if (getStyle ('bottomRightRadius') != null)        { _bottomRightRadius = getStyle ('bottomRightRadius'); }        else { _bottomRightRadius = DEFAULT_BOTTOMRIGHTRADIUS; }
                }
                // if all individual corners have their default value, the cornerRadius is used as style for the 4 corners
                else
                {
                     _topLeftRadius = getStyle ('cornerRadius');
                     _topRightRadius = getStyle ('cornerRadius');
                     _bottomLeftRadius = getStyle ('cornerRadius');
                     _bottomRightRadius = getStyle ('cornerRadius');
                }

                // ready to draw!
                var _g:Graphics = graphics;
                var _m:Matrix = new Matrix();
                _m.createGradientBox (w, h, Math.PI*_angle/180, _offsetx, _offsety);
                
                _g.clear ();
                
                // so, draw !
                _g.beginGradientFill (_gradientType, _fillColors, _fillAlphas, _gradientRatio, _m, _spreadMethod, _interpolationMethod, _focalPointRatio);
                _g.drawRoundRectComplex (0, 0, w, h, _topLeftRadius, _topRightRadius, _bottomLeftRadius, _bottomRightRadius);
                _g.endFill();
            }
        }
        
        /**
         * Returns an array containing a homogeneous gradient of colors.
         * 
         * @param n:Number The numbor of color in the gradient 
         */
        public function generateDefaultRatio (n:Number) :Array
        {
            var avg:Number = 255 / (n -1);
            var arr:Array = new Array();
            
            for (var i:Number = 0; i < n; i++)
            {
                var currentratio:Number = 255 - avg * i;
                arr.push (currentratio);
                arr.sort (Array.NUMERIC);
            }
            
            return arr;
        }
        
        // Define flag to indicate that a style property changed.
        private var bStylePropChanged:Boolean = true;
        
        /**
         * Overrides the super Class method to detect changes to the new style properties.
         * 
         * @param styleProp:String The name of the style property, or null if all styles for this component have changed.
         */
        override public function styleChanged (styleProp:String) :void
        {
            super.styleChanged (styleProp);

            // Check to see if style changed. 
            if (styleProp=="fillColors" || styleProp=="fillAlphas" || styleProp=="cornerRadius" || styleProp=="angle" || styleProp=="spreadMethod" ||  
            styleProp=="gradientType" || styleProp=="gradientRatio" || styleProp=="offsetX" || styleProp=="offsetY" || styleProp=="interpolationMethod" || 
            styleProp=="topLeftRadius" || styleProp=="topRightRadius" || styleProp=="bottomLeftRadius" || styleProp=="bottomRightRadius" || styleProp=="focalPointRatio")
            {
                bStylePropChanged = true; 
                invalidateDisplayList();
                return;
            }
        }

    }
    
}