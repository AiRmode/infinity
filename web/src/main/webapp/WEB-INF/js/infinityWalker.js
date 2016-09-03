/**
 * Created by alexey on 22.08.16.
 */
(function () {
    'use strict';

    var jArray, jStack, jDataConfig;
    var w = window;
    requestAnimationFrame = w.requestAnimationFrame || w.webkitRequestAnimationFrame || w.msRequestAnimationFrame || w.mozRequestAnimationFrame;

//getDataJSON('testStateJSON');
    getDataJSON('qvga256StateJSON').then(function (json) {
        jArray = json.array;
        jStack = json.stack;
        jDataConfig = json.dataConfig;
        startProcess();
    });

    function getDataJSON(urlPath) {
        return $.getJSON(urlPath, {}, function (json) {
            return json;
        });
    }

    function startProcess() {
        walk();
        requestAnimationFrame(startProcess);
    }

    function walk() {
        while (jStack.length != 0) {
            var frame = jStack.pop();
            jStack.push(frame);
            if (frame.index == jArray.length) {
                createImage(jArray, jDataConfig);
                jStack.pop();
                break;
            }

            if (frame.value < jDataConfig.elementMaxValue) {
                jArray[frame.index] = ++frame.value;
                var nextIndex = frame.index;
                while (nextIndex != jArray.length) {
                    jStack.push({index: nextIndex + 1, value: jDataConfig.elementMinValue});
                    nextIndex++;
                }
            } else {
                jArray[frame.index] = jDataConfig.elementMinValue;
                jStack.pop();
            }
        }
    }

    function createImage(array, dataConfig) {
        // create an offscreen canvas
        var canvas = document.getElementById("canvas");
        var ctx = canvas.getContext("2d");

        // size the canvas to your desired image
        canvas.width = dataConfig.width;
        canvas.height = dataConfig.height;

        // get the imageData and pixel array from the canvas
        var imgData = ctx.getImageData(0, 0, canvas.width, canvas.height);
        var data = imgData.data;

        // manipulate some pixel elements
        var arrayPointer = 0;
        for (var i = 0; i < data.length; i += 4) {
            data[i] = ((array[arrayPointer] + -dataConfig.elementMinValue) & 0xE0); // set every red pixel element to 255
            data[i + 1] = (((array[arrayPointer] + -dataConfig.elementMinValue) & 0x1C) << 3); // set every green pixel element to 255
            data[i + 2] = (((array[arrayPointer] + -dataConfig.elementMinValue) & 0x3) << 6); // set every blue pixel element to 255
            data[i + 3] = 255; // make this pixel opaque
            arrayPointer++;
        }

        // put the modified pixels back on the canvas
        ctx.putImageData(imgData, 0, 0);

        // create a new img object
        var image = new Image();

        // set the img.src to the canvas data url
        image.src = canvas.toDataURL();

        ctx.drawImage(image, 0, 0);
    }

})();