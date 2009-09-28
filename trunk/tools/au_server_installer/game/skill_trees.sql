-- ----------------------------
-- Table structure for `skill_trees`
-- ----------------------------
DROP TABLE IF EXISTS `skill_trees`;
CREATE TABLE `skill_trees` (
  `class_id` int(10) unsigned NOT NULL default '0',
  `skillId` int(10) unsigned NOT NULL default '0',
  `skillLevel` int(10) unsigned NOT NULL default '0',
  `name` varchar(40) NOT NULL default '',
  `type` int(10) unsigned NOT NULL default '0',
  `min_level` int(10) unsigned NOT NULL default '0',
  PRIMARY KEY  (`class_id`,`skillId`,`skillLevel`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of skill_trees
-- ----------------------------
INSERT INTO `skill_trees` VALUES ('0', '1', '1', 'Basic Sword Training', '0', '1');
INSERT INTO `skill_trees` VALUES ('0', '3', '1', 'Basic Mace Training', '0', '1');
INSERT INTO `skill_trees` VALUES ('0', '4', '1', 'Basic Clothing Proficiency', '0', '1');
INSERT INTO `skill_trees` VALUES ('0', '5', '1', 'Basic Leather Armor Proficiency', '0', '1');
INSERT INTO `skill_trees` VALUES ('0', '6', '1', 'Basic Chain Armor Proficiency', '0', '1');
INSERT INTO `skill_trees` VALUES ('0', '7', '1', 'Basic Shield Training', '0', '1');
INSERT INTO `skill_trees` VALUES ('0', '8', '1', 'Advanced Sword Training I', '0', '9');
INSERT INTO `skill_trees` VALUES ('0', '9', '1', 'Advanced Dagger Training I', '0', '9');
INSERT INTO `skill_trees` VALUES ('0', '10', '1', 'Advanced Mace Training I', '0', '9');
INSERT INTO `skill_trees` VALUES ('0', '12', '1', 'Quality Leather Armor Proficiency I', '0', '9');
INSERT INTO `skill_trees` VALUES ('0', '13', '1', 'Quality Chain Armor Proficiency I', '0', '9');
INSERT INTO `skill_trees` VALUES ('0', '14', '1', 'Advanced Shield Training I', '0', '9');
INSERT INTO `skill_trees` VALUES ('0', '15', '1', 'Advanced Great Sword Training I', '0', '9');
INSERT INTO `skill_trees` VALUES ('0', '16', '1', 'Advanced Polearm Training I', '0', '9');
INSERT INTO `skill_trees` VALUES ('0', '17', '1', 'Advanced Archery Training I', '0', '9');
INSERT INTO `skill_trees` VALUES ('0', '18', '1', 'Quality Plate Armor Proficiency I', '0', '9');
INSERT INTO `skill_trees` VALUES ('0', '67', '1', 'Basic Cloth Armor Proficiency', '0', '1');
INSERT INTO `skill_trees` VALUES ('0', '112', '1', 'Boost Physical Attack I', '0', '1');
INSERT INTO `skill_trees` VALUES ('0', '120', '1', 'Boost Parry I', '1', '9');
INSERT INTO `skill_trees` VALUES ('0', '121', '1', 'Boost HP I', '1', '5');
INSERT INTO `skill_trees` VALUES ('0', '151', '1', 'Weakening Severe Blow I', '1', '5');
INSERT INTO `skill_trees` VALUES ('0', '154', '1', 'Shield Counterattack I', '1', '9');
INSERT INTO `skill_trees` VALUES ('0', '161', '1', 'Shouting I', '1', '7');
INSERT INTO `skill_trees` VALUES ('0', '165', '1', 'Resolute Strike I', '1', '3');
INSERT INTO `skill_trees` VALUES ('0', '169', '1', 'Severe Strike I', '0', '1');
INSERT INTO `skill_trees` VALUES ('0', '173', '1', 'Shield Defense I', '1', '3');
INSERT INTO `skill_trees` VALUES ('0', '1801', '1', 'Return', '0', '1');
INSERT INTO `skill_trees` VALUES ('0', '1803', '1', 'Bandage Heal', '0', '1');
INSERT INTO `skill_trees` VALUES ('3', '1', '1', 'Basic Sword Training', '0', '1');
INSERT INTO `skill_trees` VALUES ('3', '2', '1', 'Basic Dagger Training', '0', '1');
INSERT INTO `skill_trees` VALUES ('3', '4', '1', 'Basic Clothing Proficiency', '0', '1');
INSERT INTO `skill_trees` VALUES ('3', '5', '1', 'Basic Leather Armor Proficiency', '0', '1');
INSERT INTO `skill_trees` VALUES ('3', '8', '1', 'Advanced Sword Training I', '0', '9');
INSERT INTO `skill_trees` VALUES ('3', '9', '1', 'Advanced Dagger Training I', '0', '9');
INSERT INTO `skill_trees` VALUES ('3', '12', '1', 'Quality Leather Armor Proficiency I', '0', '9');
INSERT INTO `skill_trees` VALUES ('3', '17', '1', 'Advanced Archery Training I', '0', '9');
INSERT INTO `skill_trees` VALUES ('3', '19', '1', 'Advanced Dual-Wielding', '1', '5');
INSERT INTO `skill_trees` VALUES ('3', '67', '1', 'Basic Cloth Armor Proficiency', '0', '1');
INSERT INTO `skill_trees` VALUES ('3', '112', '1', 'Evasion Rate increase I', '1', '9');
INSERT INTO `skill_trees` VALUES ('3', '113', '1', 'Boost Accuracy I', '1', '5');
INSERT INTO `skill_trees` VALUES ('3', '360', '2', 'Equip Off Hand Weapon II', '1', '9');
INSERT INTO `skill_trees` VALUES ('3', '551', '1', 'Surprice Attack I', '1', '3');
INSERT INTO `skill_trees` VALUES ('3', '555', '1', 'Counterslash I', '1', '5');
INSERT INTO `skill_trees` VALUES ('3', '559', '1', 'Hide I', '1', '5');
INSERT INTO `skill_trees` VALUES ('3', '564', '1', 'Swift Edge I', '0', '1');
INSERT INTO `skill_trees` VALUES ('3', '568', '1', 'Soul Slash I', '1', '7');
INSERT INTO `skill_trees` VALUES ('3', '572', '1', 'Focused Evasion I', '0', '1');
INSERT INTO `skill_trees` VALUES ('3', '577', '1', 'Devotion I', '1', '9');
INSERT INTO `skill_trees` VALUES ('3', '1801', '1', 'Return', '0', '1');
INSERT INTO `skill_trees` VALUES ('3', '1803', '1', 'Bandage Heal', '0', '1');
INSERT INTO `skill_trees` VALUES ('6', '4', '1', 'Basic Clothing Proficiency', '0', '1');
INSERT INTO `skill_trees` VALUES ('6', '64', '1', 'Basic Spellbook Training', '0', '1');
INSERT INTO `skill_trees` VALUES ('6', '67', '1', 'Basic Cloth Armor Proficiency', '0', '1');
INSERT INTO `skill_trees` VALUES ('6', '68', '1', 'Advanced Orb Training I', '0', '9');
INSERT INTO `skill_trees` VALUES ('6', '70', '1', 'Advanced Cloth Armor Proficiency I', '0', '9');
INSERT INTO `skill_trees` VALUES ('6', '71', '1', 'Advanced Spellbook Training I', '0', '9');
INSERT INTO `skill_trees` VALUES ('6', '105', '1', 'Concentration I', '1', '3');
INSERT INTO `skill_trees` VALUES ('6', '1099', '1', 'Restraint I', '0', '1');
INSERT INTO `skill_trees` VALUES ('6', '1351', '1', 'Flame Arrow I', '0', '1');
INSERT INTO `skill_trees` VALUES ('6', '1355', '1', 'Cold Wave', '1', '7');
INSERT INTO `skill_trees` VALUES ('6', '1358', '1', 'Erosion I', '1', '3');
INSERT INTO `skill_trees` VALUES ('6', '1362', '1', 'Ice Chain I', '1', '3');
INSERT INTO `skill_trees` VALUES ('6', '1366', '1', 'Blaze', '1', '5');
INSERT INTO `skill_trees` VALUES ('6', '1370', '1', 'Energy Absorption I', '1', '9');
INSERT INTO `skill_trees` VALUES ('6', '1377', '1', 'Stone Skin I', '1', '7');
INSERT INTO `skill_trees` VALUES ('6', '1801', '1', 'Return', '0', '1');
INSERT INTO `skill_trees` VALUES ('6', '1803', '1', 'Bandage Heal', '0', '1');
INSERT INTO `skill_trees` VALUES ('9', '3', '1', 'Basic Mace Training', '0', '1');
INSERT INTO `skill_trees` VALUES ('9', '4', '1', 'Basic Clothing Proficiency', '0', '1');
INSERT INTO `skill_trees` VALUES ('9', '5', '1', 'Basic Leather Armor Proficiency', '0', '1');
INSERT INTO `skill_trees` VALUES ('9', '10', '1', 'Advanced Mace Training I', '0', '9');
INSERT INTO `skill_trees` VALUES ('9', '12', '1', 'Quality Leather Armor Proficiency I', '0', '9');
INSERT INTO `skill_trees` VALUES ('9', '13', '1', 'Quality Chain Armor Proficiency I', '0', '9');
INSERT INTO `skill_trees` VALUES ('9', '14', '1', 'Advanced Shield Training I', '0', '9');
INSERT INTO `skill_trees` VALUES ('9', '53', '1', 'Advanced Staff Training I', '0', '9');
INSERT INTO `skill_trees` VALUES ('9', '67', '1', 'Basic Cloth Armor Proficiency', '0', '1');
INSERT INTO `skill_trees` VALUES ('9', '70', '1', 'Advanced Cloth Armor Proficiency I', '0', '9');
INSERT INTO `skill_trees` VALUES ('9', '408', '1', 'Blessing of Life I', '1', '3');
INSERT INTO `skill_trees` VALUES ('9', '955', '1', 'Blessing of Protection I', '1', '5');
INSERT INTO `skill_trees` VALUES ('9', '958', '1', 'Light of Renewal I', '1', '5');
INSERT INTO `skill_trees` VALUES ('9', '962', '1', 'Condemnation I', '1', '3');
INSERT INTO `skill_trees` VALUES ('9', '965', '1', 'Healing Light I', '0', '1');
INSERT INTO `skill_trees` VALUES ('9', '969', '1', 'Promise of Wind I', '1', '9');
INSERT INTO `skill_trees` VALUES ('9', '972', '1', 'Light of Resurrection I', '1', '9');
INSERT INTO `skill_trees` VALUES ('9', '975', '1', 'Retribution of Earth I', '0', '1');
INSERT INTO `skill_trees` VALUES ('9', '979', '1', 'Heavens Judgement I', '1', '7');
INSERT INTO `skill_trees` VALUES ('9', '1801', '1', 'Return', '0', '1');
INSERT INTO `skill_trees` VALUES ('9', '1803', '1', 'Bandage Heal', '0', '1');
