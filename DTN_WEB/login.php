<?php

include_once './conexao.php';

$json = NULL;
$lista = array();
header("Content-type:application/json");
$json = file_get_contents('php://input');
if ($json != NULL) {
    $json = json_decode($json);
    $nome = $json->nome;
    $telefone = $json->telefone;
    $query = mysqli_query($con, "SELECT * FROM cliente_rede WHERE nome LIKE '" . $nome . "' AND telefone LIKE '" . $telefone . "'");
    if ($l = mysqli_fetch_array($query)) {
        array_push($lista, $l);
    } else {
        mysqli_query($con, "INSERT INTO cliente_rede(nome,telefone) VALUES('" . $nome . "','" . $telefone . "')");
        $query = mysqli_query($con, "SELECT * FROM cliente_rede WHERE nome LIKE '" . $nome . "' AND telefone LIKE '" . $telefone . "'");
        if ($l = mysqli_fetch_array($query)) {
            array_push($lista, $l);
        }
    }
    echo json_encode($lista);
} else {
    echo "Nenhum dado foi informado";
}
