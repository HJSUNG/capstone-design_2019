<?php
    $command = escapeshellcmd('python python_test.py');
    $output = shell_exec($command);
    echo $output;
?>