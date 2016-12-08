'use strict';
/*jshint node:true*/

const fs = require('fs');
const crc = require('crc');

fs.writeFileSync('.updater-data.txt', crc.crc32(fs.readFileSync('thehansen addons.jar')).toString(10));